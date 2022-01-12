package com.paytm.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        System.out.println("post user");
        try{
            User user = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }

    @GetMapping(path="/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id){
        System.out.println("get user "+id);
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException(id)
        );
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser,@PathVariable Integer id){
        User updatedUser = userRepository.findById(id).map(user -> {
            user.setUserName(newUser.getUserName());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setMobile(newUser.getMobile());
            user.setAddress1(newUser.getAddress1());
            user.setAddress2(newUser.getAddress2());
            user.setEmail(newUser.getEmail());
            return user;
        }).orElseThrow(() ->
                new UserNotFoundException(id)
        );
        try{
            User user = userRepository.save(updatedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted");
        }else{
            throw new UserNotFoundException(id);
        }
    }

}
