package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.dto.UserRequestDto;
import com.paytm.assignment1.dto.UserResponseDto;
import com.paytm.assignment1.modals.User;
import com.paytm.assignment1.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public BaseResponseDto addUser(@RequestBody UserRequestDto requestDto) {
        logger.trace("/user addUser() request");
        User addedUser = userService.addUser(requestDto.getUserModal());
        return BaseResponseDto.builder()
                .status(HttpStatus.CREATED)
                .msg("new user created")
                .data(new UserResponseDto(addedUser))
                .build();
    }

    @GetMapping(path="/all")
    @ResponseBody
    public BaseResponseDto getAllUsers(){
        logger.trace("/user/all getAllUsers() request");
        Iterable<User> users = userService.getAllUsers();
        List<UserResponseDto> userDtos = new ArrayList<>();
        users.forEach(user -> {
            userDtos.add(new UserResponseDto(user));
        });
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(userDtos)
                .build();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BaseResponseDto getUser(@PathVariable Integer id){
        logger.trace("/user/{id} getUser() request");
        User user = userService.getUser(id);
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(new UserResponseDto(user))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public BaseResponseDto updateUser(@RequestBody UserRequestDto requestDto,@PathVariable Integer id) {
        logger.trace("/user/{id} updateUser() request");
        User updatedUser = userService.updateUser(id,requestDto.getUserModal());
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(new UserResponseDto(updatedUser))
                .msg("user details updated")
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public BaseResponseDto deleteUser(@PathVariable Integer id){
        logger.trace("/user/{id} deleteUser() request");
        userService.deleteUser(id);
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .msg("user deleted successfully")
                .build();
    }

}
