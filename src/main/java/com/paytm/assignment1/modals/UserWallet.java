package com.paytm.assignment1.modals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserWallet extends Timestamps{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
    private User user;

    private double balanceAmount;

    private boolean isActive;

    @OneToMany(mappedBy = "payer")
    private List<Transaction> payerTransactions;

    @OneToMany(mappedBy = "payee")
    private List<Transaction> payeeTransactions;

}
