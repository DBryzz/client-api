package com.bryzz.clientapi.domain.controller;

import com.bryzz.clientapi.domain.dto.AppSourceDTO;
import com.bryzz.clientapi.domain.dto.AppSourcePostDTO;
import com.bryzz.clientapi.domain.dto.UserDTO;
import com.bryzz.clientapi.domain.model.AppSource;
import com.bryzz.clientapi.domain.service.AppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api/apps")
//@SessionAttributes("sessionInfo")
public class AppController {

    /*@Autowired
    private FileStorageService storageService;*/

    private AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

   /* @GetMapping("/all")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('BUYER')" )
    public String showProducts(@ModelAttribute AppSource appSource, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "redirect:/pages/cssandjs/products";
        }

        model.addAttribute("applications", appService.getAllApplications("getDefault"));

        return "cssandjs/products";
    }
*/



    @PostMapping("/user/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public String uploadApp(@PathVariable("userId") Long userId,
                                                         @ModelAttribute @Valid AppSourcePostDTO appSource,
                                                         BindingResult result,
                                                         HttpServletRequest request,
                                                         RedirectAttributes redirectAttributes,
                                                         @RequestPart("image") MultipartFile sourceCode) {

        String message = "";


        HttpSession session = request.getSession(false);
        UserDTO userSessionDTO = (UserDTO) session.getAttribute("userSessionObj");


        if (result.hasErrors()) {
            message = "Please verify that all fields are properly filled";
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/d/app-form";
        }


        // Normalize file name
        String fileName = StringUtils.cleanPath(sourceCode.getOriginalFilename());

        if(fileName.contains("..")) {
            message = "Error: Filename contains invalid path sequence ---> " + fileName;
            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/d/app-form";
        }

        if (!(fileName.toLowerCase().endsWith(".jar") ||
                fileName.toLowerCase().endsWith(".zip") )) {
            message = "Error: UnSupported file Format. \nfile must be jar, or zip format  ---> " + fileName;

            redirectAttributes.addFlashAttribute("errorMsg", message);

            return "redirect:/pages/d/app-form";
        }

        message = appService.saveApplication(userId, appSource, sourceCode);

        if (message.contains("Error")) {
            redirectAttributes.addFlashAttribute("errorMsg", message);
        } else {
            redirectAttributes.addFlashAttribute("successMsg", message);
        }


        return "redirect:/pages/d";

    }

    /*
    @PostMapping("/user-profile/{username}/update-app/{productId}")
    public String updateProduct(@PathVariable("username") String username, @PathVariable("productId") Long productId,
                                HttpServletRequest request, Model model, @ModelAttribute @Valid AppSourcePostDTO appSource,
                                RedirectAttributes redirectAttributes, @RequestPart("image") MultipartFile productImage) {
        String message = "";
        HttpSession session = request.getSession(false);

        UserDTO userSessionDTO = (UserDTO) session.getAttribute("userSessionObj");
        message = appService.updateApplication(userSessionDTO.getUserId(), productId, appSource, productImage, request);


        if (message.contains("Error")) {
            redirectAttributes.addFlashAttribute("errorMsg", message);
        } else {
            redirectAttributes.addFlashAttribute("successMsg", message);
        }

//        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("products", appService.getAllApplicationsOwnedBy(userSessionDTO.getUserId(), "default"));
        return "redirect:/pages/user-profile/{username}/modifiedDateDsc";
    }
     */

    @GetMapping("/all")
    public ResponseEntity<List<AppSourceDTO>> getListOfAllApps() {

        return ResponseEntity.ok(appService.getAllApplications("getDefault"));
    }


    @GetMapping("/{appId}")
    @ResponseBody
    public ResponseEntity<AppSourceDTO> getApp(@PathVariable("appId") Long id) {

        return ResponseEntity.ok(appService.getApplication(id));
    }


    @GetMapping("/src/main/resources/static/src-code-dir/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = appService.loadImage(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/remove/user/{userId}/app/{appId}")
    @ResponseBody
    public String deleteProduct(@PathVariable("appId") Long id, HttpServletRequest request) {
        /*System.out.println(request.getRemoteAddr() + "hkhkhkhkh");
        System.out.println(request.getHeader("host") + "hkhkhkhkh");*/
        HttpSession session = request.getSession();
        UserDTO userSessionDTO = (UserDTO) session.getAttribute("userSessionObj");
        appService.removeApplication(id, request);

        return "redirect:/pages/user-profile/" + userSessionDTO.getUsername();
    }

}
