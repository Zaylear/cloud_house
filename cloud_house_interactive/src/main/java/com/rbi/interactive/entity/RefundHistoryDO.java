package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "refund_history")
@Data
public class RefundHistoryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "toll_collector_id",columnDefinition = "varchar(50) comment '收费人编号，当前收费客户人员的UserId'")
    private String tollCollectorId;

    @Column(name = "payer_phone",columnDefinition = "varchar(50) comment '缴费人手机号'")
    private String payerPhone;

    @Column(name = "payer_name",columnDefinition = "varchar(50) comment '缴费人姓名'")
    private String payerName;

    @Column(name = "charge_type",columnDefinition = "tinyint(4) comment '支付类型：1 物业费，2'")
    private String chargeType;

    @Column(name = "payment_method",columnDefinition = "varchar(50) comment '支付方式：支付宝扫码，微信扫码，微信支付，现金'")
    private String paymentMethod;

    @Column(name = "refund_status",columnDefinition = "tinyint(4) comment '退款状态：1 已退，0 未退；默认：0'")
    private Integer refundStatus;

    @Column(name = "invalid_state",columnDefinition = "tinyint(4) comment '失效状态：1 失效，0 有效；默认：0'")
    private Integer invalidState;

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

    @Column(name = "customer_user_id",columnDefinition = "varchar(20) comment '客户ID，唯一标识'")
    private String customerUserId;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '姓氏'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "room_code",columnDefinition = "varchar(60) comment '房间编号'")
    private String roomCode;

    @Column(name = "room_size",columnDefinition = "double comment '住房大小 单位：平方米'")
    private Double roomSize;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号'")
    private String chargeCode;

    @Column(name = "charge_name",columnDefinition = "varchar(20) comment '项目名称'")
    private String chargeName;

    @Column(name = "actual_money_collection",columnDefinition = "double comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

    @Column(name = "mortgage_amount",columnDefinition = "double comment '被扣金额'",scale = 2)
    private Double mortgageAmount;

    @Column(name = "reason_for_deduction",columnDefinition = "text comment '被扣原因'")
    private String reasonForDeduction;

    @Column(name = "refundable_amount",columnDefinition = "double comment '可退还金额'",scale = 2)
    private Double refundableAmount;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "start_time",columnDefinition = "date comment '装修开始日期'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '装修结束日期'")
    private String dueTime;

    @Column(name = "delay_time",columnDefinition = "int comment '延迟时长，单位：天'")
    private Integer delayTime;

    @Column(name = "delay_reason",columnDefinition = "text comment '延期原因'")
    private String delayReason;

    @Column(name = "person_liable",columnDefinition = "varchar(100) comment '责任人'")
    private String personLiable;

    @Column(name = "person_liable_phone",columnDefinition = "varchar(18) comment '责任人电话'")
    private String personLiablePhone;

    @Column(name = "responsible_agencies",columnDefinition = "varchar(200) comment '负责机构'")
    private String responsibleAgencies;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间，也是实收时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;
}
