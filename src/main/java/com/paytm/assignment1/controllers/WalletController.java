package com.paytm.assignment1.controllers;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create-wallet/{id}")
    public BaseResponseDto createWallet(@PathVariable int id){
        return BaseResponseDto.builder()
                .data(walletService.createWallet(id))
                .status(HttpStatus.CREATED)
                .msg("user wallet created")
                .build();
    }

}
