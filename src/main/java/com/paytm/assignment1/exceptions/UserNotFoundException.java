package com.paytm.assignment1.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Object id){
        super("User not Found with id "+id);
    }
}
