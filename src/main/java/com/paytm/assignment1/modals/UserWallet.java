package com.paytm.assignment1.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
    private User user;

    private double balanceAmount;

    private boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "payer")
    private List<Transaction> payerTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "payee")
    private List<Transaction> payeeTransactions;

}
