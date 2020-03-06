package com.rbi.admin.entity.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class WxUserInfoDTO implements Serializable{
    private String realName;
    private String mobilePhone;
    private String sex;



}
