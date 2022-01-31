package com.paytm.assignment1.dto;

import com.paytm.assignment1.enums.TransactionStatus;
import com.paytm.assignment1.enums.TransactionType;
import com.paytm.assignment1.modals.Transaction;
import lombok.Data;

import java.util.Date;

@Data
public class UserTransactionResponseDto {

    private int transactionId;
    private TransactionStatus status;
    private TransactionType type;
    private int otherUserWalletId;
    private double amount;
    private double closingBalance;
    private Date createTime;
    private Date updateTime;

    public UserTransactionResponseDto(Transaction transaction, int userWalletId){
        this.status = transaction.getStatus();
        this.createTime = transaction.getCreateTime();
        this.updateTime = transaction.getUpdateTime();
        this.amount = transaction.getAmount();
        this.transactionId = transaction.getId();
        if(transaction.getPayee().getId() == userWalletId){
            this.type = TransactionType.RECEIVE;
            this.closingBalance = transaction.getPayeeClosingBalance();
            this.otherUserWalletId = transaction.getPayer().getId();
        }
        else{
            this.type = TransactionType.SENT;
            this.closingBalance = transaction.getPayerClosingBalance();
            this.otherUserWalletId = transaction.getPayee().getId();
        }
    }

}
