package com.paytm.assignment1.dto;

import com.paytm.assignment1.modals.User;
import lombok.Data;

@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String userName;
    private String address1;
    private String address2;
    private String password;

    public User getUserModal(){
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setMobile(this.mobile);
        user.setEmail(this.email);
        user.setUserName(this.userName);
        user.setAddress1(this.address1);
        user.setAddress2(this.address2);
        user.setPassword(this.password);
        return user;
    }
}
