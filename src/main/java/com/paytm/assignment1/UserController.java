package com.paytm.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        System.out.println("post user");
        System.out.println(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(newUser));
    }

    @GetMapping(path="/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser,@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id,newUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

}
