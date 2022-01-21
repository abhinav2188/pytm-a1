package com.paytm.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;
    // for configuring authentication
    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // for configuring authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/profile").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/user").permitAll()
                .antMatchers(HttpMethod.GET,"/user").permitAll()
                .antMatchers(HttpMethod.PATCH,"/user").permitAll()
                .antMatchers(HttpMethod.DELETE,"/user").permitAll()
                .antMatchers("/").permitAll()
                .and().formLogin();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
