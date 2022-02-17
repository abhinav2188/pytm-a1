package com.paytm.assignment1;

import com.paytm.assignment1.controllers.TransactionController;
import com.paytm.assignment1.controllers.UserController;
import com.paytm.assignment1.controllers.WalletController;
import com.paytm.assignment1.repositories.TransactionRepository;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import com.paytm.assignment1.services.TransactionService;
import com.paytm.assignment1.services.UserService;
import com.paytm.assignment1.services.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletController walletController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void contextLoads(){
        assertThat(userController).isNotNull();
        assertThat(userService).isNotNull();
    }

}
