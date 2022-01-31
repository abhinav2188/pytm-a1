package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.AddTransactionRequestDto;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.modals.Transaction;
import com.paytm.assignment1.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping
    public BaseResponseDto getTransactions(){
        return BaseResponseDto.builder()
                .msg("all transactions")
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping
    public BaseResponseDto addTransaction(@RequestBody AddTransactionRequestDto requestDto){
        Transaction transaction = transactionService.addTransaction(requestDto.getPayerMobile(), requestDto.getPayeeMobile(), requestDto.getAmount());
        return BaseResponseDto.builder()
                .status(HttpStatus.CREATED)
                .data(transaction)
                .msg("created transaction")
                .build();
    }

    @GetMapping("/{mobile}")
    public BaseResponseDto getTransaction(@PathVariable String mobile){
        return BaseResponseDto.builder()
                .data(transactionService.getTransactions(mobile))
                .status(HttpStatus.OK)
                .build();
    }
}
