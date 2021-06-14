package com.bryzz.clientapi.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PagesController {

    @GetMapping("/p/home")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/p/login-form")
    public String showLoginPage() {
        return "pages/login";
    }

    @GetMapping("/p/registration-form")
    public String showRegistrationPage() {
        return "pages/register";
    }

    @GetMapping("/d")
    public String showDashboardPage() {
        return "pages/dashboard-index";
    }

    @GetMapping("/d/table")
    public String showTabularView() {
        return "pages/tables";
    }


}
