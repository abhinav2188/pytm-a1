package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyFieldException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public EmptyFieldException(String fieldName){
        super(fieldName+ " can't be null or empty");
        logger.warn("Empty Field: " + fieldName);
    }
}
