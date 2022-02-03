package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.AuthenticateRequestDto;
import com.paytm.assignment1.dto.AuthenticationResponseDto;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/greeting")
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

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public @ResponseBody
    BaseResponseDto createAuthenticationToken(@RequestBody AuthenticateRequestDto requestDto){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUserName(),requestDto.getPassword())
            );
        }catch(BadCredentialsException ex){
            return BaseResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorMsg("invalid username or password")
                    .build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUserName());
        String jwt = jwtUtil.generateToken(userDetails);

        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(new AuthenticationResponseDto(jwt))
                .build();
    }
}
