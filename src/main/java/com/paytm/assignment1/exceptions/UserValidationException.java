package com.paytm.assignment1.exceptions;



public class UserValidationException extends RuntimeException{

    private String propertyName;
    private String errorMsg;

    public UserValidationException(String propertyName, String errorMsg){
        super("Error in field: " +propertyName+ ", "+ errorMsg);
        this.propertyName = propertyName;
        this.errorMsg = errorMsg;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
