package com.paytm.assignment1.services;

import com.paytm.assignment1.enums.TransactionStatus;
import com.paytm.assignment1.exceptions.TransactionNotFoundException;
import com.paytm.assignment1.exceptions.WalletNotFoundException;
import com.paytm.assignment1.modals.Transaction;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.repositories.TransactionRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Transaction addTransaction(String payerMob, String payeeMob, double amount){
       logger.trace("addTransaction()");
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
            //failed trans event to kafka
            kafkaTemplate.send("transactions","Failed Transaction : Amount "+amount+" from "+payerMob+ " wallet to "+payeeMob+" wallet");
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

        transaction = transactionRepository.save(transaction);

        // success trans event to kafka
        kafkaTemplate.send("transactions","Success Transaction : Amount "+amount+" from "+payerMob+ " wallet to "+payeeMob+" wallet");

        return transaction;
    }

    public List<Transaction> getTransactions(int walletId, int pageNo){
        logger.trace("getTransactions()");
        Pageable pages = PageRequest.of(pageNo,2, Sort.by("create_time").descending());
        System.out.println("TransactionService: getTransactions()");
        return transactionRepository.findAllByWalletId(walletId,pages);
    }

    public Transaction getTransaction(int txnId){
        logger.trace("getTransaction()");
        return transactionRepository.findById(txnId).orElseThrow(() -> new TransactionNotFoundException(txnId));
    }

    @KafkaListener(topics = "transactions", groupId = "user-wallet-group")
    public void transactionsKafkaListener(String msg){
        System.out.println("Received kafka event in transactions: "+msg);
    }

}
