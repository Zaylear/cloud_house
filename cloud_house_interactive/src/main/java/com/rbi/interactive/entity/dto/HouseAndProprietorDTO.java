package com.rbi.interactive.entity.dto;

import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HouseAndProprietorDTO implements Serializable {

    public final static long serialVersionUID = 1L;

    String organizationId;
    String organizationName;

    private String villageCode;
    private String villageName;

    private String regionCode;
    private String regionName;
    private String buildingCode;
    private String buildingName;
    private String unitCode;
    private String unitName;

    private String roomCode;
    private Double roomSize;
    private String customerUserId;
    private String mobilePhone;
    private String surname;
    private String sex;
    private String idNumber;
    private Double surplus;
    private String dueTime;
    private Integer roomType;
    private String quarterlyCycleTime;
    private Integer minMonth;//欠费月数
    private String startBillingTime;
    private Double prepaidAmount; //预缴金额
    private Integer prepaidMonths;//预缴月数
    private Integer identity;
    private List<ParkingSpaceManagementDO> parkingSpaceManagementDOS;
    private Double oneMonthPropertyFee;//单月物业费
    private Double amountOfArrears;//欠费金额
}
