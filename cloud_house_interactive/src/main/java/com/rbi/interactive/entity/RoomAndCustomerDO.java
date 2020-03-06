package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room_customer")
@Data
public class RoomAndCustomerDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_user_id",columnDefinition = "varchar(20) comment '用户ID，唯一标识'")
    private String customerUserId;

    @Column(name = "room_code",columnDefinition = "varchar(50) comment '房间编号'")
    private String roomCode;

    @Column(name = "identity",columnDefinition = "tinyint(4) comment '身份：1 业主；2 副业主；3 租客'")
    private Integer identity;

    @Column(name = "past_due",columnDefinition = "tinyint(4) comment '过期状态：1、已过期 0、未过期 指租房时间过期'")
    private Integer pastDue;

    @Column(name = "logged_off_state",columnDefinition = "tinyint(4) comment '注销状态：1、已注销 0、未注销 指注销房间'")
    private Integer loggedOffState;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "normal_payment_status",columnDefinition = "tinyint(1) comment '正常缴费状态：1、正常 0、不正常 不正常说明可能存在空置费等问题'")
    private Integer normalPaymentStatus;

    @Column(name = "surplus",columnDefinition = "double(20,2) comment '余额'",scale = 2)
    private Double surplus;

    /**
     *添加业主从无到有在到无的各个时间节点
     */
    @Column(name = "start_time",columnDefinition = "date comment '租房开始时间'")
    private String  startTime;

    @Column(name = "end_time",columnDefinition = "date comment '租房结束时间'")
    private String endTime;

    @Column(name = "delivery_time_of_house",columnDefinition = "date comment '交房日期'")
    private String deliveryTimeOfHouse;

    @Column(name = "recycling_home_time",columnDefinition = "date comment '收房日期'")
    private String recyclingHomeTime;

    @Column(name = "real_delivery_time_of_house",columnDefinition = "date comment '实际交房日期'")
    private String realDeliveryTimeOfHouse;

    @Column(name = "real_recycling_home_time",columnDefinition = "date comment '实际收房日期'")
    private String realRecyclingHomeTime;

    @Column(name = "payment_time",columnDefinition = "date comment '揭下款时间'")
    private String paymentTime;

    @Column(name = "filing_time",columnDefinition = "date comment '备案时间'")
    private String filingTime;

    @Column(name = "start_billing_time",columnDefinition = "date comment '开始计物业费时间'")
    private String startBillingTime;

    @Column(name = "quarterly_cycle_time",columnDefinition = "date comment '季度循环周期的时间'")
    private String quarterlyCycleTime;

    @Column(name = "open_id",columnDefinition = "varchar(100) comment '微信号'")
    private String openId;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime comment '更新时间'")
    private String udt;
}
