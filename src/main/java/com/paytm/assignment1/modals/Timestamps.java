package com.paytm.assignment1.modals;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class Timestamps {

    // for converting java.util.data to sql version of timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // EntityListener before an entity is created
    @PrePersist
    private void onCreate(){
        createTime = updateTime = new Date();
    }

    // EntityListener before an entity is updated
    @PreUpdate
    private void onUpdate(){
        updateTime = new Date();
    }

}
