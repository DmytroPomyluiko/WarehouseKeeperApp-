package com.warehousekeeper.root.controllers;



import com.warehousekeeper.root.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {


    @GetMapping("/show")
    @ResponseBody
    public String showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getUsername());
        return personDetails.getUsername();
    }


   /* @GetMapping("/admin")
    public String adminPage(){
        adminService.doAdminStuff();
        return "admin";
    }*/
}
