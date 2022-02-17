package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateFieldException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public DuplicateFieldException(String fieldName, String value){
        super(fieldName+": "+value+" is already taken, should be unique");
        logger.warn("Duplicate Field :"+fieldName);
    }
}
