package com.paytm.assignment1.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserValidationException extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String propertyName;
    private String errorMsg;

    public UserValidationException(String propertyName, String errorMsg){
        super("Error in field: " +propertyName+ ", "+ errorMsg);
        this.propertyName = propertyName;
        this.errorMsg = errorMsg;
        logger.warn(propertyName);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
