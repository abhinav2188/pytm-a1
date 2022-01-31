package com.paytm.assignment1.controllers;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.dto.UserResponseDto;
import com.paytm.assignment1.modals.User;
import com.paytm.assignment1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public BaseResponseDto addUser(@RequestBody User newUser) {
        User addedUser = userService.addUser(newUser);
        return BaseResponseDto.builder()
                .status(HttpStatus.CREATED)
                .msg("new user created")
                .data(addedUser)
                .build();
    }

    @GetMapping(path="/all")
    @ResponseBody
    public BaseResponseDto getAllUsers(){
        Iterable<User> users = userService.getAllUsers();
//        List<UserResponseDto> userDtos = new ArrayList<>();
//        users.forEach(user -> {
//            userDtos.add(new UserResponseDto(user));
//        });
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(users)
                .build();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BaseResponseDto getUser(@PathVariable Integer id){
        User user = userService.getUser(id);
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(new UserResponseDto(user))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public BaseResponseDto updateUser(@RequestBody User newUser,@PathVariable Integer id) {
        User updatedUser = userService.updateUser(id,newUser);
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .data(new UserResponseDto(updatedUser))
                .msg("user details updated")
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public BaseResponseDto deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return BaseResponseDto.builder()
                .status(HttpStatus.OK)
                .msg("user deleted successfully")
                .build();
    }

}
