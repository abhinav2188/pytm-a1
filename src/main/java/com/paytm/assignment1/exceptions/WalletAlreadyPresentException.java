package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalletAlreadyPresentException extends RuntimeException{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public WalletAlreadyPresentException(Object id){
        super("wallet already present with id: "+id);
        logger.warn(String.valueOf(id));
    }
}
