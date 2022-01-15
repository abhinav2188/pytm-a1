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

}
