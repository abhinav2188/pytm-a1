package com.paytm.assignment1.services;

import com.paytm.assignment1.exceptions.UserNotFoundException;
import com.paytm.assignment1.exceptions.WalletAlreadyPresentException;
import com.paytm.assignment1.modals.User;
import com.paytm.assignment1.modals.UserWallet;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public UserWallet createWallet(int userId){
        User myUser = userRepository.findById(userId).map( user -> {
            if(user.getWallet() != null)
                throw new WalletAlreadyPresentException(userId);
            UserWallet userWallet = new UserWallet();
            userWallet.setActive(true);
            userWallet.setBalanceAmount(0);
            userWallet.setUser(user);
            user.setWallet(userWallet);
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException(userId));
        return myUser.getWallet();
    }







}
