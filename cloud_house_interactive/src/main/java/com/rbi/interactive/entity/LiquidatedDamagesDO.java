package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "liquidated_damages")
@Data
public class LiquidatedDamagesDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

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

    @Column(name = "Building_name",columnDefinition = "varchar(50) comment '楼宇名称'")
    private String buildingName;

    @Column(name = "unit_code",columnDefinition = "varchar(20) comment '单元编号'")
    private String unitCode;

    @Column(name = "unit_name",columnDefinition = "varchar(20) comment '单元名称'")
    private String unitName;

    @Column(name = "room_code",columnDefinition = "varchar(100) comment '房间编号'")
    private String roomCode;

    @Column(name = "room_size",columnDefinition = "float(16,4) comment '房间面积'")
    private Double roomSize;

    @Column(name = "customer_user_id",columnDefinition = "varchar(20) comment '用户ID，唯一标识'")
    private String customerUserId;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '姓氏'")
    private String surname;

    @Column(name = "mobile_phone",columnDefinition = "varchar(100) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "liquidated_damages",columnDefinition = "text comment '违约金详情'")
    private String liquidatedDamages;

    @Column(name = "amount_total_receivable",columnDefinition = "double comment '应收金额'",scale = 2)
    private Double amountTotalReceivable;

    @Column(name = "actual_total_money_collection",columnDefinition = "double comment '实收金额'",scale = 2)
    private Double actualTotalMoneyCollection;

    @Column(name = "one_month_property_fee_amount",columnDefinition = "double comment '一个月的物业费金额'",scale = 2)
    private Double oneMonthPropertyFeeAmount;

    @Column(name = "surplus_total",columnDefinition = "double comment '减免总违约金'",scale = 2)
    private Double surplusTotal;

    @Column(name = "surplus_reason",columnDefinition = "text comment '减免原因'")
    private Double surplusReason;

    @Column(name = "audit_status",columnDefinition = "tinyint comment '审核状态'")
    private Integer auditStatus;

    @Column(name = "toll_collector_id",columnDefinition = "varchar(50) comment '操作人'")
    private String tollCollectorId;

    @Column(name = "toll_collector_name",columnDefinition = "varchar(50) comment '操作人姓名'")
    private String tollCollectorName;

    @Column(name = "superfluous_amount",columnDefinition = "double comment '物业费除整月缴费以外多余的金额'")
    private Double superfluousAmount;

    @Column(name = "reviser_id",columnDefinition = "varchar(50) comment '修订人'")
    private String reviserId;

    @Column(name = "reviser_name",columnDefinition = "varchar(50) comment '修订人姓名'")
    private String reviserName;

    @Column(name = "audit_id",columnDefinition = "varchar(50) comment '审核人'")
    private String auditId;

    @Column(name = "audit_name",columnDefinition = "varchar(50) comment '审核人姓名'")
    private String auditName;

    @Column(name = "retrial_id",columnDefinition = "varchar(50) comment '复审人'")
    private String retrialId;

    @Column(name = "retrial_name",columnDefinition = "varchar(50) comment '复审人姓名'")
    private String retrialName;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间，也是实收时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "quarterly_cycle_time",columnDefinition = "date comment '循环周期时间'")
    private String quarterlyCycleTime;

    @Column(name = "property_actual_money_collection",columnDefinition = "double comment '物业费实收金额'",scale = 2)
    private Double propertyActualMoneyCollection;

    @Column(name = "month",columnDefinition = "int comment '物业费月数'")
    private Integer month;

    @Column(name = "liquidated_damage_due_time",columnDefinition = "varchar(50) comment '违约金到期时间'")
    private String liquidatedDamageDueTime;
}
