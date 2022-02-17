package com.paytm.assignment1.services;

import com.paytm.assignment1.exceptions.DuplicateFieldException;
import com.paytm.assignment1.exceptions.EmptyFieldException;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.modals.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean isNullOrEmpty(String obj){
        return (obj==null || obj.length() == 0);
    }

    private boolean validateUser(User user){
        logger.debug("validating new user");
        //request validation
        if(isNullOrEmpty(user.getFirstName())) throw new EmptyFieldException("firstName");
        if(isNullOrEmpty(user.getUserName())) throw new EmptyFieldException("userName");
        if(isNullOrEmpty(user.getEmail())) throw new EmptyFieldException("email");
        if(isNullOrEmpty(user.getPassword())) throw new EmptyFieldException("password");
        if(isNullOrEmpty(user.getMobile())) throw new EmptyFieldException("mobile");
        if(isNullOrEmpty(user.getAddress1())) throw new EmptyFieldException("address1");
        // business validation
        if(userRepository.existsByMobile(user.getMobile())) throw new DuplicateFieldException("mobile",user.getMobile());
        if(userRepository.existsByEmail(user.getEmail())) throw new DuplicateFieldException("email",user.getEmail());
        if(userRepository.existsByUserName(user.getUserName())) throw new DuplicateFieldException("userName",user.getUserName());
        logger.debug("user validations done");
        return true;
    }

    private User validatedUpdateUser(User user, User updateUser){

        logger.debug("validating fields for updating user");
        // updating values only if they are not null
        if(!isNullOrEmpty(updateUser.getFirstName()))
            user.setFirstName(updateUser.getFirstName());
        if(!isNullOrEmpty(updateUser.getLastName()))
            user.setLastName(updateUser.getLastName());
        if(!isNullOrEmpty(updateUser.getPassword()))
            user.setPassword(updateUser.getPassword());
        if(!isNullOrEmpty(updateUser.getAddress1()))
            user.setAddress1(updateUser.getAddress1());
        if(!isNullOrEmpty(updateUser.getAddress2()))
            user.setAddress2(updateUser.getAddress2());

        // checking if the updatedUser name is null, if null let the previous one..else
        // check if it is same as the old one, it is not taken by another user
        if(!isNullOrEmpty(updateUser.getUserName())){
            if(updateUser.getUserName().compareTo(user.getUserName()) != 0){
                if(userRepository.existsByUserName(updateUser.getUserName()))
                    throw new DuplicateFieldException("userName",updateUser.getUserName());
                user.setUserName(updateUser.getUserName());
            }
        }

        if(!isNullOrEmpty(updateUser.getMobile())){
            if(updateUser.getMobile().compareTo(user.getMobile()) != 0){
                if(userRepository.existsByMobile(updateUser.getMobile()))
                    throw new DuplicateFieldException("mobile",updateUser.getMobile());
                user.setMobile(updateUser.getMobile());
            }
        }

        if(!isNullOrEmpty(updateUser.getEmail())){
            if(updateUser.getEmail().compareTo(user.getEmail())!=0){
                if(userRepository.existsByEmail(updateUser.getEmail()))
                    throw new DuplicateFieldException("Email",updateUser.getEmail());
                user.setEmail(updateUser.getEmail());
            }
        }

        logger.debug("validation complete");
        return user;
    }


    public User addUser(User newUser){
        logger.trace("addUser()");
        validateUser(newUser);
        newUser.setActive(true);
        newUser.setRoles("USER");
        return userRepository.save(newUser);
    }

    public User getUser(int id){
        logger.trace("getUser()");
        return userRepository.findById(id).orElseThrow( () -> new UserNotFoundException(id));
    }

    public Iterable<User> getAllUsers(){
        logger.trace("getAllUsers()");
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(int id, User newUser){
        logger.trace("updateUser()");
        return userRepository.findById(id).map( user -> {
            user = validatedUpdateUser(user,newUser);
            return userRepository.save(user);
        }).orElseThrow( () -> new UserNotFoundException(id));
    }

    @Transactional
    public boolean deleteUser(int id){
        logger.trace("deleteUser()");
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        if(userRepository.setUserActiveState(id,false)  == 0) return false;
        return true;
    }


}
