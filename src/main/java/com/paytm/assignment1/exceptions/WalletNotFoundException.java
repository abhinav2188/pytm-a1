package com.paytm.assignment1.exceptions;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(Object id){
        super("wallet not found with userId: "+id);
    }
}
