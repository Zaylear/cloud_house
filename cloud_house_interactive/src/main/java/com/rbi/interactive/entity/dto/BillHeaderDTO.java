package com.rbi.interactive.entity.dto;

import com.rbi.interactive.entity.CostDeductionDO;
import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class BillHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String organizationId;
    private String printDate;
    private String buildingName;
    private String roomCode;
    private String surname;
    private String mobilePhone;
    private String dueTime;
    private Double surplus;
    private String payerPhone;
    private String payerName;
    private Double roomSize;
    private Double amountTotalReceivable;
    private Double actualTotalMoneyCollection;
    private String remark;//备注
//    private Double deductibleSurplus;
    private String tollCollectorName;
    private String realGenerationTime;//订单生成时间

    ArrayList<BillSubjectDTO> billSubjectDTOS;
    ArrayList<CostDeductionDO> costDeductionDOS;
    ArrayList<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOS;


}
