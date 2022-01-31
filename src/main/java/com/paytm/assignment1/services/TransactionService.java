package com.paytm.assignment1.services;

import com.paytm.assignment1.enums.TransactionStatus;
import com.paytm.assignment1.exceptions.WalletNotFoundException;
import com.paytm.assignment1.modals.Transaction;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.repositories.TransactionRepository;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;


    public Transaction addTransaction(String payerMob, String payeeMob, double amount){

        // checking if both have their wallets
        UserWallet payerWallet = walletRepository.findByUserMobile(payerMob)
                .orElseThrow( () -> new WalletNotFoundException(payerMob));
        UserWallet payeeWallet = walletRepository.findByUserMobile(payeeMob)
                .orElseThrow( () -> new WalletNotFoundException(payeeMob));

        // creating a new Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setPayer(payerWallet);
        transaction.setPayee(payeeWallet);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction = transactionRepository.save(transaction);

        // checking balance
        if(payerWallet.getBalanceAmount() < amount){
            transaction.setStatus(TransactionStatus.FAILED);
            return transactionRepository.save(transaction);
        }

        // updating payer,payee balance
        payerWallet.setBalanceAmount(payerWallet.getBalanceAmount() - amount);
        payeeWallet.setBalanceAmount(payeeWallet.getBalanceAmount() + amount);
        walletRepository.save(payeeWallet);
        walletRepository.save(payerWallet);

        // updating transaction
        transaction.setPayerClosingBalance(payerWallet.getBalanceAmount());
        transaction.setPayeeClosingBalance(payeeWallet.getBalanceAmount());
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(String mobile){
        System.out.println("TransactionService: getTransactions()");
        UserWallet wallet = walletRepository.findByUserMobile(mobile)
                .orElseThrow( () -> new WalletNotFoundException(mobile));
        return (List<Transaction>) transactionRepository.findAllByWalletId(wallet.getId());
    }

}
