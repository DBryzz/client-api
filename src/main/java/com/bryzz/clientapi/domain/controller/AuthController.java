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

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String signIntoAccount(@ModelAttribute @Valid @RequestBody UserLoginDTO loginRequest, BindingResult result,
                                  RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response ) {

        String message = "";

        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/authentication/is-gsm/";
        }

        HttpSession session = request.getSession(true);
        UserDTO userDTO = userService.loginUser(loginRequest, response);
        session.setAttribute("userSessionObj", userDTO);




            System.out.print("User does not exist");
            message = "Successful login";

            redirectAttributes.addFlashAttribute("successMsg", message);
//            model.addAttribute("hasGSM", Boolean.toString(hasGSM));

            return "redirect:/pages/register/is-gsm/";

    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute @Valid UserPostDTO createUserDTO,
                             BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        String message = "";
        boolean gsmOrNot = false;

        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/register";
        }

        UserDTO userDTO = userService.createUser(createUserDTO);
        message = "Account created successfully";

        model.addAttribute("newUser", userDTO);
        redirectAttributes.addFlashAttribute("successMsg", message);


        model.addAttribute("newUser", new UserDTO());

        return "redirect:/pages/authentication/is-gsm/";
    }

    @GetMapping("/logout/{userId}")
    String logoutUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") String currentAuthUserId){

        userService.logoutUser(request, response);

//        HttpSession session = request.getSession(false);
//        session.invalidate();


        return "redirect:/pages/home";
    }

}
