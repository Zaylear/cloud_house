package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "bill")
@Data
public class BillDO implements Serializable {

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

    @Column(name = "room_size",columnDefinition = "double(16,4) comment '房间面积'")
    private Double roomSize;

    @Column(name = "customer_user_id",columnDefinition = "varchar(20) comment '客户UserId，唯一标识'")
    private String customerUserId;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '客户姓名'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(100) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "payer_phone",columnDefinition = "varchar(50) comment '缴费人手机号'")
    private String payerPhone;

    @Column(name = "payer_name",columnDefinition = "varchar(50) comment '缴费人姓名'")
    private String payerName;

    @Column(name = "amount_total_receivable",columnDefinition = "double(20,4) comment '应收总金额'",scale = 2)
    private Double amountTotalReceivable;

    @Column(name = "actual_total_money_collection",columnDefinition = "double(20,4) comment '实收总金额'",scale = 2)
    private Double actualTotalMoneyCollection;

//    @Column(name = "preferential_total_amount",columnDefinition = "double(20,4) comment '总优惠金额'",scale = 2)
//    private Double preferentialTotalAmount;

    @Column(name = "toll_collector_id",columnDefinition = "varchar(50) comment '收费人编号，当前收费客户人员的UserId'")
    private String tollCollectorId;

    @Column(name = "payment_method",columnDefinition = "varchar(50) comment '支付方式：支付宝扫码，微信扫码，微信支付，现金'")
    private String paymentMethod;

    @Column(name = "corrected_amount",columnDefinition = "double(20,4) comment '修正金额'",scale = 2)
    private Double correctedAmount;

//    @Column(name = "state_of_arrears",columnDefinition = "tinyint(4) comment '欠费状态：0 未欠费，1 欠费；默认：0'")
//    private Integer stateOfArrears;

    @Column(name = "refund_status",columnDefinition = "tinyint(4) comment '退款状态：1 已退，0 未退；默认：0'")
    private Integer refundStatus;

    @Column(name = "invalid_state",columnDefinition = "tinyint(4) comment '失效状态：0 有效，1 无效；默认：1'")
    private Integer invalidState;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

//    @Column(name = "order_generation_date",columnDefinition = "datetime(0) comment '订单生成日期'")
//    private String orderGenerationDate;

    @Column(name = "real_generation_time",columnDefinition = "datetime(0) comment '真正形成订单的时间'")
    private String realGenerationTime;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;
}
