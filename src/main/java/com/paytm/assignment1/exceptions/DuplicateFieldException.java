package com.paytm.assignment1.exceptions;

public class DuplicateFieldException extends RuntimeException{
    public DuplicateFieldException(String fieldName, String value){
        super(fieldName+": "+value+" is already taken, should be unique");
    }
}
