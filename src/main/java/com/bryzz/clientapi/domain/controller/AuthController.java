package com.bryzz.clientapi.domain.controller;

import com.bryzz.clientapi.domain.dto.UserDTO;
import com.bryzz.clientapi.domain.dto.UserLoginDTO;
import com.bryzz.clientapi.domain.dto.UserPostDTO;
import com.bryzz.clientapi.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/mocked/login")
    public String mockedSignIntoAccount(@ModelAttribute @Valid UserLoginDTO userLoginDTO, BindingResult result,
                                  RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response ) {

        String message = "";

        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/p/login-form";
        }

        logger.info("{}", userLoginDTO);

        String name = userLoginDTO.getUsername();
        String paswd = userLoginDTO.getPassword();

        UserDTO userDTO = new UserDTO();

        if(name.toLowerCase().equals("bryzz") && paswd.toLowerCase().equals("bryzz123")) {
            userDTO.setUsername(name);
            userDTO.setUserId(1l);
            userDTO.setEmail("domoubrice@gmail.com");
            userDTO.setFirstName("Domou");
            userDTO.setLastName("Brice");
            userDTO.setRoles("ROLE_USER ROLE_DEPLOYER ROLE_ADMIN");

            List<String> sRoles = new ArrayList<>();
            sRoles.add("ROLE_USER");
            sRoles.add("ROLE_DEPLOYER");
            sRoles.add("ROLE_ADMIN");

            userDTO.setSRoles(sRoles);
            userDTO.setAccessToken("accesstoken2domoubrice1");
        }

        if(name.toLowerCase().equals("dbryzz") && paswd.toLowerCase().equals("dbryzz123")) {
            userDTO.setUsername(name);
            userDTO.setUserId(2l);
            userDTO.setEmail("domou.brice@yahoo.com");
            userDTO.setFirstName("Namou");
            userDTO.setLastName("Armel");
            userDTO.setRoles("ROLE_USER ROLE_DEPLOYER");

            List<String> sRoles = new ArrayList<>();
            sRoles.add("ROLE_USER");
            sRoles.add("ROLE_DEPLOYER");

            userDTO.setSRoles(sRoles);


            userDTO.setSRoles(sRoles);
            userDTO.setAccessToken("accesstoken2NamouArmel2");
        }

        if(name.toLowerCase().equals("cat") && paswd.toLowerCase().equals("cat123")) {
            userDTO.setUsername(name);
            userDTO.setUserId(3l);
            userDTO.setEmail("cat@meow.to");
            userDTO.setFirstName("Cat");
            userDTO.setLastName("Meow");
            userDTO.setRoles("ROLE_USER");

            List<String> sRoles = new ArrayList<>();
            sRoles.add("ROLE_USER");

            userDTO.setSRoles(sRoles);

            userDTO.setSRoles(sRoles);
            userDTO.setAccessToken("accesstoken2catmeow3");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userSessionObj", userDTO);

        logger.info("{}", userDTO);

        message = "Successful login";
        logger.info(message);

        redirectAttributes.addFlashAttribute("successMsg", message);
//            model.addAttribute("hasGSM", Boolean.toString(hasGSM));

        return "redirect:/pages/p/home";

    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute @Valid UserPostDTO createUserDTO,
                             BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        String message = "";
        boolean gsmOrNot = false;

        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/p/registration-form";
        }

        UserDTO userDTO = userService.createUser(createUserDTO);
        message = "Account created successfully";

        model.addAttribute("newUser", userDTO);
        redirectAttributes.addFlashAttribute("successMsg", message);


        model.addAttribute("newUser", new UserDTO());

        logger.info("{}", userDTO);

        return "redirect:/pages/p/login-form";
    }

    @PostMapping("/login")
    public String signIntoAccount(@ModelAttribute @Valid UserLoginDTO userLoginDTO, BindingResult result,
                                  RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response ) {

        String message = "";

        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/p/login-form";
        }

        logger.info("{}", userLoginDTO);

        HttpSession session = request.getSession(true);
        UserDTO userDTO = userService.loginUser(userLoginDTO, response);
        session.setAttribute("userSessionObj", userDTO);

        System.out.print("User does not exist");
        message = "Successful login";

        redirectAttributes.addFlashAttribute("successMsg", message);
//            model.addAttribute("hasGSM", Boolean.toString(hasGSM));

        return "redirect:/pages/p/home";

    }


    @GetMapping("/logout/{userId}")
    String logoutUser(HttpServletRequest request, HttpServletResponse response,
                      @PathVariable("userId") String currentAuthUserId, RedirectAttributes redirectAttributes ){

        String message = "";
        userService.logoutUser(request, response);

        message = "Account created successfully";

        redirectAttributes.addFlashAttribute("successMsg", message);

        return "redirect:/pages/p/home";
    }

}
