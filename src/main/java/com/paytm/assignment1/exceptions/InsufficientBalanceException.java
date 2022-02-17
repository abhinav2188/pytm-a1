package com.paytm.assignment1.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsufficientBalanceException extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public InsufficientBalanceException(String payeeMob) {
        super("Insufficient Balance Amount");
        logger.warn(payeeMob);
    }
}
