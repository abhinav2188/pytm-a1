package com.paytm.assignment1.services;

import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.exceptions.UserValidationException;
import com.paytm.assignment1.modals.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private boolean validateUser(User user){
        //request validation
        if(user.getFirstName() == null || user.getFirstName() == "") throw new UserValidationException("firstName","property can't be null or empty");
        if(user.getEmail() == null || user.getEmail() == "") throw new UserValidationException("email","property can't be null or empty");
        if(user.getMobile() == null || user.getMobile() == "") throw new UserValidationException("mobile","property can't be null or empty");
        if(user.getUserName() == null || user.getUserName() == "") throw new UserValidationException("userName","property can't be null or empty");
        if(user.getAddress1() == null || user.getAddress1() == "") throw new UserValidationException("address1","property can't be null or empty");
        if(user.getPassword() == null || user.getPassword() == "") throw new UserValidationException("password","property can't be null or empty");
        // business validation
        if(userRepository.findByUserName(user.getUserName()).isPresent()) throw new UserValidationException("userName","already taken, should be unique");
        if(userRepository.findByMobile(user.getMobile()).isPresent()) throw new UserValidationException("mobile","already registered, should be unique");
        if(userRepository.findByEmail(user.getEmail()).isPresent()) throw new UserValidationException("email","already registered, should be unique");
        return true;
    }

    public User addUser(User newUser){
        validateUser(newUser);
        newUser.setActive(true);
        newUser.setRoles("USER");
        return userRepository.save(newUser);
    }

    public User getUser(int id){
        return userRepository.findById(id).orElseThrow( () -> new UserNotFoundException(id));
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(int id, User newUser){
        return userRepository.findById(id).map( user -> {
            user.setUserName(newUser.getUserName());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setMobile(newUser.getMobile());
            user.setAddress1(newUser.getAddress1());
            user.setAddress2(newUser.getAddress2());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            validateUser(user);
            return userRepository.save(user);
        }).orElseThrow( () -> new UserNotFoundException(id));
    }

    public boolean deleteUser(int id){
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        userRepository.deleteById(id);
        return true;
    }


}
