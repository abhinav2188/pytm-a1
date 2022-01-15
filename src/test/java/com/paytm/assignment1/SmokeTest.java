package com.paytm.assignment1;

import com.paytm.assignment1.controllers.UserController;
import com.paytm.assignment1.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads(){
        assertThat(userController).isNotNull();
        assertThat(userRepository).isNotNull();
    }

}
