package com.rbi.interactive.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BillsBackDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private String organizationId;
}
