package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalletNotFoundException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public WalletNotFoundException(Object id){
        super("wallet not found with userId: "+id);
        logger.warn(String.valueOf(id));
    }
}
