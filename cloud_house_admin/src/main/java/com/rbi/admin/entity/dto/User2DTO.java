package com.rbi.admin.entity.dto;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data


public class User2DTO {

    private Integer id;
    private String organizationId;
    private String organizationName;
    private String userId;
    private String username;
    private String password;
    private String salt;
    private String realName;
    private String sex;
    private String birthday;
    private String email;
    private String address;
    private String mobilePhone;
    private Integer enabled;
    private String identity;
    private String departmentId;
    private String portraitPath;
    private Integer loginStatus;
    private String idt;
    private String udt;

}
