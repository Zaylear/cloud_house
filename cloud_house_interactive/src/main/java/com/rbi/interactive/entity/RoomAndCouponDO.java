package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room_coupon")
@Data
public class RoomAndCouponDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "village_code",columnDefinition = "varchar(50) comment '小区编号'")
    private String villageCode;

    @Column(name = "village_name",columnDefinition = "varchar(50) comment '小区名称'")
    private String villageName;

    @Column(name = "region_code",columnDefinition = "varchar(20) comment '地块编号'")
    private String regionCode;

    @Column(name = "region_name",columnDefinition = "varchar(50) comment '地块名称'")
    private String regionName;

    @Column(name = "building_code",columnDefinition = "varchar(20) comment '楼宇编号'")
    private String buildingCode;

    @Column(name = "building_name",columnDefinition = "varchar(50) comment '楼宇名称'")
    private String buildingName;

    @Column(name = "unit_code",columnDefinition = "varchar(20) comment '单元编号'")
    private String unitCode;

    @Column(name = "unit_name",columnDefinition = "varchar(20) comment '单元名称'")
    private String unitName;

    @Column(name = "room_code",columnDefinition = "varchar(50) comment '房间代码'")
    private String roomCode;

    @Column(name = "coupon_code",columnDefinition = "varchar(50) comment '优惠卷编号'")
    private String couponCode;

    @Column(name = "coupon_name",columnDefinition = "varchar(100) comment '优惠卷名称'")
    private String couponName;

    @Column(name = "customer_user_id",columnDefinition = "varchar(50) comment '客户ID，唯一标识'")
    private String customerUserId;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '姓氏'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "money",columnDefinition = "double(20,2) comment '金额'")
    private Double money;

    @Column(name = "property_fee",columnDefinition = "double(20,2) comment '抵扣物业费金额'")
    private Double propertyFee;

//    @Column(name = "balance_amount",columnDefinition = "double(20,2) comment '剩余余额金额'")
//    private Double balanceAmount;

    @Column(name = "usage_state",columnDefinition = "tinyint(4) comment '使用状态：1、已使用 0、未使用'")
    private Integer usageState;

    @Column(name = "past_due",columnDefinition = "tinyint(4) comment '过期状态：1、已过期 0、未过期'")
    private Integer pastDue;

    @Column(name = "effective_time",columnDefinition = "double comment '有效时长  单位: 天 '")
    private Integer effectiveTime;

    @Column(name = "audit_status",columnDefinition = "tinyint(1) comment '审核状态: 1-待审核，2-待复审，3-完成审核'")
    private Integer auditStatus;

    @Column(name = "start_time",columnDefinition = "datetime(0) comment '开始时间'")
    private String startTime;

    @Column(name = "end_time",columnDefinition = "datetime(0) comment '过期时间'")
    private String endTime;

    @Column(name = "coupon_type",columnDefinition = "varchar(20) comment '优惠卷类型'")
    private String couponType;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号,微信端识别物业费收费项目'")
    private String chargeCode;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间即获取金券时间！'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

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
}
