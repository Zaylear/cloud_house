package com.rbi.admin.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
@Data
public class PropertyDiscountDTO implements Serializable{
    private double chargeStandard;
    private String chargeName;
    private String chargeCode;
    private HashMap<Integer,String> discount;


}
