package com.paytm.assignment1.modals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paytm.assignment1.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends Timestamps{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_wallet_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserWallet payer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="payee_wallet_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserWallet payee;

    private double amount;

    // pending,completed,failed
    private TransactionStatus status;

    private double payerClosingBalance;

    private double payeeClosingBalance;

}
