package com.paytm.assignment1.modals;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// modal class for user details , helps in spring security (getting users logging in)
public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean isActive;

    public MyUserDetails(User user){
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.isActive = user.isActive();
        this.authorities = new ArrayList<>();
        String tmp[] = user.getRoles().split(",");
        for(String t:tmp){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+t));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("authorities: "+authorities);
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}