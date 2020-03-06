package com.rbi.admin.entity.dto.houseinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class HouseInfoDTO<T,S>implements Serializable{
    private S HouseInfo;
    private List<T> propertyList;
    private List<T> tenantList;
}
