package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="history_data")
public class HistoryDataDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_code",columnDefinition = "varchar(50) comment '房间编号'")
    private String roomCode;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "year",columnDefinition = "date comment '年'")
    private String year;

    @Column(name = "month",columnDefinition = "date comment '月'")
    private String month;

    @Column(name = "day",columnDefinition = "date comment '日'")
    private String day;

    @Column(name = "status",columnDefinition = "tinyint(4) comment '总计：1、总计 0、分计，即按年月日计'")
    private String  status;

    @Column(name = "room_size",columnDefinition = "double(20,2) comment '住房大小 单位：平方米'")
    private Double roomSize;

    @Column(name = "vacancy_charge",columnDefinition = "double(20,2) comment '空置费'")
    private Double vacancyCharge;

    @Column(name = "paid_in_property_fee",columnDefinition = "double(20,2) comment '实收物业费'")
    private double paidInPropertyFee;

    @Column(name = "decoration_management_fee",columnDefinition = "double(20,2) comment '装修管理费'")
    private Double decorationManagementFee;

    @Column(name = "garbage_collection_and_transportation_fee",columnDefinition = "double(20,2) comment '垃圾清运费'")
    private Double garbageCollectionAndTransportationFee;

    @Column(name = "decoration_deposit",columnDefinition = "double(20,2) comment '装修保证金'")
    private Double decorationDeposit;

    @Column(name = "refunded_decoration_deposit",columnDefinition = "double(20,2) comment '已退装修保证金'")
    private Double refundedDecorationDeposit;

    @Column(name = "deducted_decoration_deposit",columnDefinition = "double(20,2) comment '已抵扣装修保证金'")
    private double deductedDecorationDeposit;

    @Column(name = "cash_coupon",columnDefinition = "double(20,2) comment '物业代金券'")
    private double cashCoupon;

    @Column(name = "old_property_brings_new",columnDefinition = "double(20,2) comment '老带新'")
    private double oldPropertyBringsNew;

    @Column(name = "recovery_previous_debts",columnDefinition = "double(20,2) comment '收回前期欠款'")
    private Double recoveryPreviousDebts;

    @Column(name = "current_income_from_advance_receipts",columnDefinition = "double(20,2) comment '当期预收转收入'")
    private Double currentIncomeFromAdvanceReceipts;

    @Column(name = "deduction_three_way_fees",columnDefinition = "double(20,2) comment '三通费抵扣'")
    private double deductionThreeWayFees;

    @Column(name = "reduction_income",columnDefinition = "double(20,2) comment '冲减收入'")
    private double reductionIncome;

    @Column(name = "amount_of_property_fee",columnDefinition = "double(20,2) comment '抵扣前物业费（应收物业费） = 实收物业费+已抵扣装修保证金+代金券+老带新+三通费抵扣+冲减收入'")
    private Double amountOfPropertyFee;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "organization_id",columnDefinition = "varchar(20) comment '组织id'")
    private String organizationId;

    @Column(name = "delivery_time",columnDefinition = "date comment'房开交互时间'")
    private String deliveryTime;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
