package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotFoundException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public UserNotFoundException(Object id){
        super("User not Found with id "+id);
        logger.warn(String.valueOf(id));
    }
}
