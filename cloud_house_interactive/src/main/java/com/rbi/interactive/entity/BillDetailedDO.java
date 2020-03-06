package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bill_detailed")
@Data
public class BillDetailedDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billDetailedId;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号'")
    private String chargeCode;

    @Column(name = "charge_name",columnDefinition = "varchar(20) comment '项目名称'")
    private String chargeName;

    @Column(name = "charge_type",columnDefinition = "varchar(10) comment '收费类型： 相当于chargeType'")
    private String chargeType;

    @Column(name = "charge_standard",columnDefinition = "double(20,4) comment '收费标准单价'")
    private Double chargeStandard;

    @Column(name = "charge_standards",columnDefinition = "varchar(100) comment '三通费收费标准'")
    private String chargeStandards;

//    @Column(name = "display_mode_status",columnDefinition = "tinyint(4) comment '显示状态 0、显示数值 1、下拉框'")
//    private Integer displayModeStatus;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "datedif",columnDefinition = "int(11) comment '缴费月数'")
    private Integer datedif;

    @Column(name = "discount",columnDefinition = "double(20,4) comment '折扣'")
    private Double discount;

    @Column(name = "parking_space_place",columnDefinition = "varchar(100) comment '车位地点 1、地面 2、地下'")
    private String parkingSpacePlace;

    @Column(name = "parking_space_type",columnDefinition = "varchar(100) comment '车位类型：子母车位'")
    private String parkingSpaceType;

    @Column(name = "amount_receivable",columnDefinition = "double(20,4) comment '应收金额'",scale = 2)
    private Double amountReceivable;

    @Column(name = "actual_money_collection",columnDefinition = "double(20,4) comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

//    @Column(name = "preferential_amount",columnDefinition = "double(20,4) comment '优惠金额'",scale = 2)
//    private Double preferentialAmount;

    //水电费
    @Column(name = "usage_amount",columnDefinition = "double(20,4) comment '使用量'")
    private Double usageAmount;

    @Column(name = "current_readings",columnDefinition = "double(20,4) comment '当前读数'")
    private Double currentReadings;

    @Column(name = "last_reading",columnDefinition = "double(20,4) comment '上次读数'")
    private Double lastReading;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "state_of_arrears",columnDefinition = "tinyint(4) comment '欠费状态：0 未欠费，1 欠费；默认：0'")
    private Integer stateOfArrears;

//    @Column(name = "original_state_of_arrears",columnDefinition = "tinyint(4) comment '原始欠费状态：0 未欠费，1 欠费；默认：0'")
//    private Integer originalStateOfArrears;

    @Column(name = "payer_phone",columnDefinition = "varchar(50) comment '缴费人手机号'")
    private String payerPhone;

    @Column(name = "payer_name",columnDefinition = "varchar(50) comment '缴费人姓名'")
    private String payerName;

    @Column(name = "payer_user_id",columnDefinition = "varchar(50) comment '缴费人UserId'")
    private String payerUserId;

    @Column(name = "deductible_money",columnDefinition = "double(20,4) comment '可抵扣金额'")
    private Double deductibleMoney;

    @Column(name = "deductibled_money",columnDefinition = "double(20,4) comment '已抵扣金额'")
    private Double deductibledMoney;

    @Column(name = "surplus_deductible_money",columnDefinition = "double(20,4) comment '剩余可抵扣金额'")
    private Double surplusDeductibleMoney;

    @Column(name = "amount_deducted_this_time",columnDefinition = "double(20,4) comment '本次抵扣金额'")
    private Double amountDeductedThisTime;

    @Column(name = "deduction_record",columnDefinition = "varchar(100) comment '抵扣记录'",scale = 2)
    private String deductionRecord;

    @Column(name = "code",columnDefinition = "int comment '标识本次单据的唯一ID'")
    private Integer code;

    @Column(name = "parent_code",columnDefinition = "int comment '标识本次单据的父ID'")
    private Integer parentCode;

    @Column(name = "split_state",columnDefinition = "tinyint comment '拆分状态 0、未拆分 1、已拆分 2、拆分后的新数据'")
    private Integer splitState;

    @Column(name = "owner_selection",columnDefinition = "varchar(255) comment '业主选择字段，仅供前端使用，无任何业务意义'")
    private String ownerSelection;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "arrears_type",columnDefinition = "varchar(10) comment '标识欠费类型 欠费类型为18'")
    private String arrearsType;

    @Column(name = "reduction_income",columnDefinition = "double(20,2) comment '冲减收入'")
    private Double reductionIncome;

    @Column(name = "deductible_surplus",columnDefinition = "double comment '抵扣剩余 正对三通费、优惠券、保证金抵扣'")
    private Double deductibleSurplus;

}
