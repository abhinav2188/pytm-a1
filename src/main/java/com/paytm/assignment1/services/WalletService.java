package com.paytm.assignment1.services;

import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.exceptions.WalletAlreadyPresentException;
import com.paytm.assignment1.exceptions.WalletNotFoundException;
import com.paytm.assignment1.modals.User;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public UserWallet createWallet(String mobile){
        User myUser = userRepository.findByMobile(mobile).map( user -> {
            if(user.getWallet() != null)
                throw new WalletAlreadyPresentException(mobile);
            UserWallet userWallet = new UserWallet();
            userWallet.setActive(true);
            userWallet.setBalanceAmount(0);
            userWallet.setUser(user);
            user.setWallet(userWallet);
            user = userRepository.save(user);
            this.kafkaTemplate.send("create-wallet","new wallet created with mobile "+user.getMobile());
            return user;
        }).orElseThrow(() -> new UserNotFoundException(mobile));
        return myUser.getWallet();
    }

    public UserWallet addBalance(String mobile, double amount){
        UserWallet wallet = walletRepository.findByUserMobile(mobile)
                .orElseThrow(() -> new WalletNotFoundException(mobile));
        wallet.setBalanceAmount(wallet.getBalanceAmount() + amount);
        return walletRepository.save(wallet);
    }

    public UserWallet getWallet(String mobile){
        UserWallet wallet = walletRepository.findByUserMobile(mobile)
                .orElseThrow(() -> new WalletNotFoundException(mobile));
        return wallet;
    }

    @KafkaListener(topics = "create-wallet", groupId = "user-wallet-group")
    public void listenCreateWallet(String msg){
        System.out.println("Received kafka event for create-wallet : "+msg);
    }
}
