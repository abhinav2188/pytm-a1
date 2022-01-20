package com.paytm.assignment1.advices;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.exceptions.UserValidationException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserExceptionsAdvice {

    @ExceptionHandler(UserValidationException.class)
    @ResponseBody
    BaseResponseDto userValidationExceptionHandler(UserValidationException ex){
        System.out.println("user validation exception handler");
        return BaseResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorMsg(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    BaseResponseDto userNotFoundHandler(UserNotFoundException ex){
        return BaseResponseDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorMsg(ex.getMessage()).build();
    }

}
