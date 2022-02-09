package com.paytm.assignment1.exceptions;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(int id){
        super("No Transaction found with id: "+id);
    }
}
