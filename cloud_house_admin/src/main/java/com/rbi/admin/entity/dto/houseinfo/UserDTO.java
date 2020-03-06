package com.rbi.admin.entity.dto.houseinfo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserDTO implements Serializable{
    private String userId;
    private String mobilePhone;
    private String userName;
}
