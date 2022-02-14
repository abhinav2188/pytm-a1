package com.paytm.assignment1.services;

import com.paytm.assignment1.modals.MyUserDetails;
import com.paytm.assignment1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.trace("loadUserByUsername()");
        return userRepository.findByUserName(username)
                .map(MyUserDetails::new)
                .orElseThrow( () -> new UsernameNotFoundException("username not found"));
    }
}