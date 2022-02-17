package com.paytm.assignment1.advices;

import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.exceptions.DuplicateFieldException;
import com.paytm.assignment1.exceptions.EmptyFieldException;
import com.paytm.assignment1.exceptions.UserAuthorizationException;
import com.paytm.assignment1.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserExceptionsAdvice {

    @ExceptionHandler({EmptyFieldException.class, DuplicateFieldException.class})
    @ResponseBody
    BaseResponseDto userValidationExceptionHandler(RuntimeException ex){
        return BaseResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorMsg(ex.getMessage())
                .build();
    }

    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
    @ResponseBody
    BaseResponseDto userNotFoundHandler(RuntimeException ex){
        return BaseResponseDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorMsg(ex.getMessage()).build();
    }

    @ExceptionHandler({UserAuthorizationException.class})
    @ResponseBody
    BaseResponseDto userAuthorizationExceptionHandler(RuntimeException ex){
        return BaseResponseDto.builder()
                .status(HttpStatus.FORBIDDEN)
                .errorMsg(ex.getMessage())
                .build();
    }

}
