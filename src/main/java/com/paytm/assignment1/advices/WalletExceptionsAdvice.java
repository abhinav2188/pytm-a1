package com.paytm.assignment1.advices;

import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.exceptions.InsufficientBalanceException;
import com.paytm.assignment1.exceptions.TransactionNotFoundException;
import com.paytm.assignment1.exceptions.WalletAlreadyPresentException;
import com.paytm.assignment1.exceptions.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class WalletExceptionsAdvice {

    @ExceptionHandler({WalletNotFoundException.class, TransactionNotFoundException.class})
    public BaseResponseDto NotFoundsHandler(RuntimeException re){
        return BaseResponseDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorMsg(re.getMessage())
                .build();
    }

    @ExceptionHandler({WalletAlreadyPresentException.class, InsufficientBalanceException.class})
    public BaseResponseDto badRequestsHandler(RuntimeException re){
        return BaseResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorMsg(re.getMessage())
                .build();
    }
}
