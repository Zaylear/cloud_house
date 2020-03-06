package com.rbi.admin.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class Organization2DTO{

    private Integer id;
    private String organizationId;
    private String organizationName;
    private String districtCode;
    private String districtName;
    private String openId;
    private String postcode;
    private String regNo;
    private String latitude;
    private String longitude;
    private String legalPerson;
    private String address;
    private String foundDate;
    private String fax;
    private String telNumber;
    private String email;
    private String scale;
    private String category;
    private String introduction;
    private String billName;
    private String pid;
    private String pname;
    private String idt;
    private String udt;
    private List<?> organization2DTO;

}
