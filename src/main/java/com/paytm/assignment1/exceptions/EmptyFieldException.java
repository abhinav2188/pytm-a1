package com.paytm.assignment1.exceptions;

public class EmptyFieldException extends RuntimeException{
    public EmptyFieldException(String fieldName){
        super(fieldName+ " can't be null or empty");
    }
}
