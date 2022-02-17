package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionNotFoundException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public TransactionNotFoundException(int id){
        super("No Transaction found with id: "+id);
        logger.warn(String.valueOf(id));
    }
}
