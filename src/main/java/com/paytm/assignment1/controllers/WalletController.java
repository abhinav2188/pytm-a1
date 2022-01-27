package com.paytm.assignment1.controllers;

import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create-wallet/{mobile}")
    public BaseResponseDto createWallet(@PathVariable String mobile){
        return BaseResponseDto.builder()
                .data(walletService.createWallet(mobile))
                .status(HttpStatus.CREATED)
                .msg("user wallet created")
                .build();
    }

    @PostMapping("/add-balance")
    public BaseResponseDto addBalanceToWallet(@RequestParam String mobile,@RequestParam double amount){
        return BaseResponseDto.builder()
                .data(walletService.addBalance(mobile,amount))
                .status(HttpStatus.OK)
                .msg("added amount "+amount+" to wallet with mobile "+mobile)
                .build();
    }

    @GetMapping("/wallet/{mobile}")
    public BaseResponseDto getWallet(@PathVariable String mobile){
        return BaseResponseDto.builder()
                .data(walletService.getWallet(mobile))
                .status(HttpStatus.OK)
                .build();
    }

}
