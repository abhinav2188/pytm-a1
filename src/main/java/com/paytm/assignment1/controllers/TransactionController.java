package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.AddTransactionRequestDto;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.dto.UserTransactionResponseDto;
import com.paytm.assignment1.modals.Transaction;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.repositories.WalletRepository;
import com.paytm.assignment1.services.TransactionService;
import com.paytm.assignment1.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public BaseResponseDto getTransactions(@PathVariable String mobile){
        UserWallet wallet = walletService.getWallet(mobile);
        List<Transaction> userTransactions = transactionService.getTransactions(wallet.getId());
        List<UserTransactionResponseDto> responseDtos = userTransactions.stream()
                .map(t -> new UserTransactionResponseDto(t,wallet.getId()))
                .collect(Collectors.toList());

        return BaseResponseDto.builder()
                .data(responseDtos)
                .status(HttpStatus.OK)
                .build();
    }
}
