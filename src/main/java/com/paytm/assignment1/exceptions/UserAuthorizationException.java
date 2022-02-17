package com.paytm.assignment1.exceptions;

public class UserAuthorizationException extends RuntimeException {
    public UserAuthorizationException(Object userId) {
        super("User can't be authorized for userId: "+userId);
    }
}
