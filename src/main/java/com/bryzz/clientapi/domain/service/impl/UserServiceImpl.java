package com.bryzz.clientapi.domain.service.impl;

import ch.qos.logback.core.html.IThrowableRenderer;
import com.bryzz.clientapi.domain.constant.ERole;
import com.bryzz.clientapi.domain.dto.UserDTO;
import com.bryzz.clientapi.domain.dto.UserLoginDTO;
import com.bryzz.clientapi.domain.dto.UserPostDTO;
import com.bryzz.clientapi.domain.model.Role;
import com.bryzz.clientapi.domain.model.User;
import com.bryzz.clientapi.domain.repository.RoleRepository;
import com.bryzz.clientapi.domain.repository.UserRepository;
import com.bryzz.clientapi.domain.service.UserService;
import com.bryzz.clientapi.security.jwt.JwtUtils;
import com.bryzz.clientapi.security.service.UserDetailsImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDTO createUser(UserPostDTO createUserDTO) {
        if (userRepository.existsByUsername(createUserDTO.getUsername())) {
            logger.error("Error: Username is already taken!", new InvalidParameterException());
        }

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            logger.error("Error: Email is already in use!", new InvalidParameterException());
        }

        // Create new user's account

        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(encoder.encode(createUserDTO.getPassword()));

        Set<String> strRoles = createUserDTO.getRoles();
        Set<Role> roles = new HashSet<>();


        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "deployer":
                    Role deployerRole = roleRepository.findByName(ERole.ROLE_DEPLOYER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(deployerRole);

                    break;
                default:
                    break;
            }
        });

        user.setRoles(roles);
        List<String> roleList = new ArrayList<>();
        user.getRoles().forEach(role -> roleList.add(role.toString()));
        String roleString = String.join(" ", roleList);

        User savedUser = userRepository.save(user);

        UserDTO userDTO = getUserDTO(savedUser, roleList, roleString);

        logger.info("User registered successfully!");
        logger.info("{}", userDTO);

        return userDTO;
    }

    @Override
    public UserDTO loginUser(UserLoginDTO usernamePasswordDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernamePasswordDTO.getUsername(), usernamePasswordDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String roles = String.join(" ", rolesList);

        User user = userRepository.findById(userDetails.getId()).get();



        UserDTO userDTO = getUserDTO(user, rolesList, roles);
        userDTO.setAccessToken(jwt);


        return userDTO;



       /* return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));*/
    }


    @Override
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {

        /* Getting session and then invalidating it */
        HttpSession session = request.getSession(false);
        if (request.isRequestedSessionIdValid() && (session != null)) {
            session.invalidate();
        }

        HttpSession session1 = request.getSession(true);
        session1.setAttribute("JSESSIONID", null);

        handleLogoutResponse(request, response);

    }


 /*   StringBuilder roleString = new StringBuilder();

    HashSet<String> roles = new HashSet<>();

        Splitter.on(";")
                .omitEmptyStrings()
                .splitToList(user.getRoles())
            .forEach(role -> roles.add(role));

        if (isSeller) {
        roles.add("BUYER");
    }
        roles.add(newRole);*/

    @NotNull
    private UserDTO getUserDTO(User user, List<String> rolesList, String roles) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(roles);
        userDTO.setSRoles(rolesList);
        return userDTO;
    }


    /**
     * This method would edit the cookie information and
     * make JSESSIONID empty while responding to logout. This
     * would help to avoid same cookie ID each time a person logs in
     */
    public void handleLogoutResponse(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        /*Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);*/
    }
}
