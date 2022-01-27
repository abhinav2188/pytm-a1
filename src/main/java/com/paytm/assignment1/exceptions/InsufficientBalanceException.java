package com.paytm.assignment1.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String payeeMob) {
        super("Insufficient Balance Amount");
    }
}
