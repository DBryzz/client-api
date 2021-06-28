package com.bryzz.clientapi.domain.controller;

import com.bryzz.clientapi.domain.dto.ImagePostDTO;
import com.bryzz.clientapi.domain.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/pages")
public class PagesController {
    private static Logger logger = LoggerFactory.getLogger(PagesController.class);
    AppService appService;

    @Autowired
    public PagesController(AppService appService) {
        this.appService = appService;
    }

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

    @GetMapping("/d/{userId}")
    public String showDashboardPage(@PathVariable(name = "userId") Long id, Model model) {
        model.addAttribute("deployerApps", appService.getAllApplicationsOwnedBy(id, "default"));
        return "pages/dashboard-index";
    }

    /*@GetMapping("/pages/d/{userId}/apps")
    public String showAppsPage(@PathVariable(name = "userId") Long id, Model model) {
        model.addAttribute("deployerApps", appService.getAllApplicationsOwnedBy(id, "default"));
        return "pages/dashboard-index";
    }*/

    @GetMapping("/d/{userId}/apps/approved")
    public String showHostedApps(@PathVariable(name = "userId") Long id, Model model) {
        logger.info("I got here -controller");

        model.addAttribute("deployerApps", appService.getAllApplicationsWithStatusAndOwnedBy(id, "DnP"));
        return "pages/apps";
    }

    @GetMapping("/d/{userId}/apps/pending")
    public String showPendingApps(@PathVariable(name = "userId") Long id, Model model) {
        logger.info("I got here -controller");

        model.addAttribute("deployerApps", appService.getAllApplicationsWithStatusAndOwnedBy(id, "PENDING"));
        return "pages/apps";
    }

    @GetMapping("/d/{userId}/tabular")
    public String showTabularView(@PathVariable(name = "userId") Long id, Model model) {
        model.addAttribute("deployerApps", appService.getAllApplicationsOwnedBy(id, "default"));
        return "pages/tables";
    }

    @GetMapping("/d/{userId}/app-form")
    public String showAppForm() {
        return "pages/app-form";
    }

    @GetMapping("/d/{userId}/show/app/{appId}")
    public String showContainerForm(@PathVariable(name = "userId") Long id,
                                    @PathVariable(name = "appId") Long appId,
                                    @ModelAttribute @Valid ImagePostDTO imagePostDTO, Model model) {
        logger.info(System.getProperty("user.dir"));
        model.addAttribute("appSource", appService.getApplication(appId));
        return "pages/edit-deploy";
    }

   /* @GetMapping("/d/{userId}/show/app/{appId}")
    public String showAppDeployPage(@PathVariable(name = "userId") Long id, @PathVariable(name = "appId") Long appId, Model model) {
        model.addAttribute("deployerApps", appService.getApplication(appId));
        return "pages/tables";
    }*/


}
