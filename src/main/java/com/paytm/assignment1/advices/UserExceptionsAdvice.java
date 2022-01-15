package com.paytm.assignment1.advices;

import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.exceptions.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionsAdvice {

    @ExceptionHandler(UserValidationException.class)
    ResponseEntity<?> userValidationExceptionHandler(UserValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<?> userNotFoundHandler(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
