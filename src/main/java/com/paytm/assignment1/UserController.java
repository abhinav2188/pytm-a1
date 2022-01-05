package com.paytm.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public @ResponseBody String addUser(@RequestBody User user) {
        userRepository.save(user);
        return ("saved : "+user.toString());
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody User getUser(@PathVariable Integer id){
        System.out.println("get user "+id);
        return userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException(id)
        );
    }

    @PutMapping("/{id}")
    public @ResponseBody String updateUser(@RequestBody User newUser,@PathVariable Integer id){
        User updatedUser =  userRepository.findById(id)
                .map( user -> {
                    user.setEmail(newUser.getEmail());
                    user.setName(newUser.getName());
                    return userRepository.save(user);
                }).orElseGet(() -> {
                        newUser.setId(id);
                        return userRepository.save(newUser);
                    }
                );
        return ("updated User : "+updatedUser.toString());
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable Integer id){
        userRepository.deleteById(id);
        return "user deleted";
    }

}
