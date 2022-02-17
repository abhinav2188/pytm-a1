package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.AddTransactionRequestDto;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.dto.UserTransactionResponseDto;
import com.paytm.assignment1.modals.Transaction;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.services.TransactionService;
import com.paytm.assignment1.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    WalletService walletService;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/{mobile}")
    public BaseResponseDto addTransaction(@RequestBody AddTransactionRequestDto requestDto, @PathVariable String mobile){
        logger.trace("/transaction/{mobile} addTransaction()", requestDto);

        Transaction transaction = transactionService.addTransaction(mobile, requestDto.getPayeeMobile(), requestDto.getAmount());
        return BaseResponseDto.builder()
                .status(HttpStatus.CREATED)
                .data(transaction)
                .msg("created transaction")
                .build();
    }

    @GetMapping("/{mobile}")
    public BaseResponseDto getTransaction(@PathVariable String mobile, @RequestParam int pageNo){
        logger.trace("/transaction/{mobile} getTransactions()");
        UserWallet wallet = walletService.getWallet(mobile);
        List<Transaction> userTransactions = transactionService.getTransactions(wallet.getId(),pageNo);
        List<UserTransactionResponseDto> responseDtos = userTransactions.stream()
                .map(t -> new UserTransactionResponseDto(t,wallet.getId()))
                .collect(Collectors.toList());

        return BaseResponseDto.builder()
                .data(responseDtos)
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public BaseResponseDto getTransaction(@RequestParam int txnId){
        logger.trace("/transaction getTransaction");
        return BaseResponseDto.builder()
                .data(transactionService.getTransaction(txnId))
                .status(HttpStatus.OK)
                .build();
    }
}
