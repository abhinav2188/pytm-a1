package com.paytm.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private boolean validateUser(User user){
        if(user.getFirstName() == null || user.getFirstName() == "") throw new UserValidationException("firstName","property can't be null or empty");
        if(user.getEmail() == null || user.getEmail() == "") throw new UserValidationException("email","property can't be null or empty");
        if(user.getMobile() == null || user.getMobile() == "") throw new UserValidationException("mobile","property can't be null or empty");
        if(user.getUserName() == null || user.getUserName() == "") throw new UserValidationException("userName","property can't be null or empty");
        if(user.getAddress1() == null || user.getAddress1() == "") throw new UserValidationException("address1","property can't be null or empty");
        if(userRepository.findByUserName(user.getUserName()).isPresent()) throw new UserValidationException("userName","already taken, should be unique");
        if(userRepository.findByMobile(user.getMobile()).isPresent()) throw new UserValidationException("mobile","already registered, should be unique");
        if(userRepository.findByEmail(user.getEmail()).isPresent()) throw new UserValidationException("email","already registered, should be unique");
        return true;
    }

    public User addUser(User newUser){
        validateUser(newUser);
        return userRepository.save(newUser);
    }

}
