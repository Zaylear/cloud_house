package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "refund_application")
@Data
public class RefundApplicationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "refundable_amount",columnDefinition = "double comment '可退还金额'",scale = 2)
    private Double refundableAmount;

    @Column(name = "actual_money_collection",columnDefinition = "double comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

    @Column(name = "mortgage_amount",columnDefinition = "double comment '被扣金额'",scale = 2)
    private Double mortgageAmount;

    @Column(name = "transfer_card_amount",columnDefinition = "double comment '转银行卡金额'",scale = 2)
    private Double transferCardAmount;

    @Column(name = "deduction_property_fee",columnDefinition = "double comment '抵扣物业费金额'",scale = 2)
    private Double deductionPropertyFee;

//    @Column(name = "deduction_property_fee_surplus",columnDefinition = "double comment '抵扣物业费剩余金额'",scale = 2)
//    private Double deductionPropertyFeeSurplus;

    @Column(name = "deductible_money",columnDefinition = "double(20,4) comment '可抵扣金额'",scale = 2)
    private Double deductibleMoney;

    @Column(name = "deductibled_money",columnDefinition = "double(20,4) comment '已抵扣金额'",scale = 2)
    private Double deductibledMoney;

    @Column(name = "surplus_deductible_money",columnDefinition = "double(20,4) comment '剩余可抵扣金额'",scale = 2)
    private Double surplusDeductibleMoney;

    @Column(name = "amount_deducted_this_time",columnDefinition = "double(20,4) comment '本次抵扣金额'",scale = 2)
    private Double amountDeductedThisTime;

    @Column(name = "deduction_record",columnDefinition = "varchar(100) comment '抵扣记录'")
    private String deductionRecord;

    @Column(name = "audit_status",columnDefinition = "tinyint comment '审核状态'")
    private Integer auditStatus;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间，也是实收时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

}
