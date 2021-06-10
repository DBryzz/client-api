package com.bryzz.clientapi.domain.service;

import com.bryzz.clientapi.domain.dto.UserDTO;
import com.bryzz.clientapi.domain.dto.UserLoginDTO;
import com.bryzz.clientapi.domain.dto.UserPostDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    UserDTO createUser(UserPostDTO createUserDTO);

    UserDTO loginUser(UserLoginDTO usernamePasswordDTO, HttpServletResponse response);

    void logoutUser(HttpServletRequest request, HttpServletResponse response);

   /* void changeUserPassword(ChangePasswordDTO changePasswordDTO);

    void forgotUserPassword(EmailDTO emailDTO);

    UserDTO getCurrentAuthUser();

    UserDTO getUserById(String userId);

    void updateProfile(UpdateProfileDTO updateProfileDTO, HttpSession session);

    UserSessionDTO addRole(String username, UserRoles role);

    TokenDTO getNewToken(RefreshTokenDTO refreshTokenDTO);*/
}
