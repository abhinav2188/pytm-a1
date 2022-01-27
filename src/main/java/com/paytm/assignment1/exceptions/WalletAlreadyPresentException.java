package com.paytm.assignment1.exceptions;

public class WalletAlreadyPresentException extends RuntimeException{
    public WalletAlreadyPresentException(int id){
        super("wallet already present with id: "+id);
    }
}
