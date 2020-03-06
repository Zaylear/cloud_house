package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "cost_deduction")
@Data
public class CostDeductionDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer costDeductionId;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "deduction_item",columnDefinition = "varchar(50) comment '抵扣项目'")
    private String deductionItem;

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

    @Column(name = "deduction_status",columnDefinition = "tinyint(4) comment '抵扣状态：0 不抵扣，1 抵扣；默认：0'")
    private Integer deductionStatus;

    @Column(name = "original_deduction_status",columnDefinition = "tinyint(4) comment '原始抵扣状态：0 不抵扣，1 抵扣；默认：0'")
    private Integer originalDeductionStatus;

    @Column(name = "deduction_code",columnDefinition = "varchar(50) comment '抵扣编号'")
    private String deductionCode;

    @Column(name = "deduction_order_id",columnDefinition = "varchar(50) comment '抵扣订单Id 存储的是对应的数据库id标识'")
    private Integer deductionOrderId;

    @Column(name = "deduction_method",columnDefinition = "varchar(50) comment '抵扣方式: 全额抵扣'")
    private String deductionMethod;

    @Column(name = "deduction_due_time",columnDefinition = "varchar(50) comment '过期时间'")
    private String deductionDueTime;

    @Column(name = "charge_type",columnDefinition = "int comment '收费类型： 相当于chargeType'")
    private String chargeType;

    @Column(name = "discount",columnDefinition = "double(20,4) comment '折扣'")
    private Double discount;

    @Column(name = "unique_id",columnDefinition = "int(11) comment '抵扣费用的唯一标识 无业务意义'")
    private Integer uniqueId;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

}
