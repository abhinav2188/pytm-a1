package com.paytm.assignment1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public @ResponseBody String greeting(){
        return "Hello Spring!";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "<h1>hello admin!</h1>";
    }

    @GetMapping("/profile")
    public @ResponseBody String userProfile(){
        return "<h1>hello user!</h1>";
    }

}
