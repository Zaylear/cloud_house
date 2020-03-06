package com.rbi.admin.entity.dto;

import lombok.Data;

@Data
public class LogOwnerDTO {
    private String organizationId;
    private String logCode;
    private String surname;
    private String phone;
    private String roomCode;
    private Integer totalNumber;
    private Integer realNumber;
    private String result;
    private String idt;

}
