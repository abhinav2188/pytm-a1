package com.paytm.assignment1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTransactionRequestDto {
    private String payerMobile;
    private String payeeMobile;
    private double amount;
}
