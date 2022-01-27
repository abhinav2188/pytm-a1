package com.paytm.assignment1.exceptions;

public class WalletAlreadyPresentException extends RuntimeException{
    public WalletAlreadyPresentException(Object id){
        super("wallet already present with id: "+id);
    }
}
