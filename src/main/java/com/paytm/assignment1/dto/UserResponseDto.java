package com.paytm.assignment1.dto;

import com.paytm.assignment1.modals.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDto{

    private Integer id;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String userName;
    private String address1;
    private String address2;
    private Date createTime;
    private Date updateTime;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.address1 = user.getAddress1();
        this.address2 = user.getAddress2();
        this.createTime = user.getCreateTime();
        this.updateTime = user.getUpdateTime();
    }

}
