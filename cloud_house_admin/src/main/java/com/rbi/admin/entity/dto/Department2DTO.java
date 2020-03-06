package com.rbi.admin.entity.dto;



import lombok.Data;

import java.util.List;

@Data
public class Department2DTO {

    private Integer id;
    private String organizationId;
    private String organizationName;
    private String deptId;
    private String deptName;
    private String deptCategory;
    private String telNumber;
    private String fax;
    private String description;
    private String pid;
    private Integer flag;
    private String idt;
    private String udt;
    private List<?> department2DTO;
}
