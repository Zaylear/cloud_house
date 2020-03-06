package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 作废
 */

@Entity
@Table(name = "parking_space_bill")
@Data
public class ParkingSpaceBillDO implements Serializable {

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

    @Column(name = "charge_type",columnDefinition = "tinyint(4) comment '支付类型：1、物业费.住宅，2、物业费.商业'")
    private String chargeType;

    @Column(name = "payment_method",columnDefinition = "varchar(50) comment '支付方式：支付宝支付，刷卡支付，微信支付，现金支付'")
    private String paymentMethod;

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

    @Column(name = "surname",columnDefinition = "varchar(4) comment '姓氏'")
    private String surname;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "room_code",columnDefinition = "varchar(20) comment '房间号'")
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

    @Column(name = "amount_receivable",columnDefinition = "double comment '应收金额'",scale = 2)
    private Double amountReceivable;

    @Column(name = "actual_money_collection",columnDefinition = "double comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

//    @Column(name = "preferential_amount",columnDefinition = "double comment '优惠金额'",scale = 2)
//    private Double preferentialAmount;

//    @Column(name = "surplus_money",columnDefinition = "double(8,2) comment '余额抵扣金额'",scale = 2)
//    private Double surplusMoney;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "charge_standard",columnDefinition = "double(20,4) comment '收费单价'")
    private Double chargeStandard;

    @Column(name = "datedif",columnDefinition = "int(11) comment '缴费月数'")
    private Integer datedif;

    @Column(name = "discount",columnDefinition = "double(20,4) comment '折扣'")
    private Double discount;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "parking_space_nature",columnDefinition = "varchar(100) comment '车位性质 1、专有车位 2、租赁车位'")
    private String parkingSpaceNature;

    @Column(name = "parking_space_area",columnDefinition = "double comment '车位面积'")
    private Double parkingSpaceArea;

    @Column(name = "parking_space_type",columnDefinition = "varchar(100) comment '车位类型：子母车位'")
    private String parkingSpaceType;

    @Column(name = "license_plate_number",columnDefinition = "varchar(50) comment '车牌号'")
    private String licensePlateNumber;

    @Column(name = "license_plate_color",columnDefinition = "varchar(50) comment '车牌颜色'")
    private String licensePlateColor;

    @Column(name = "license_plate_type",columnDefinition = "varchar(50) comment '车牌类型 '")
    private String licensePlateType;

    @Column(name = "vehicle_original_type",columnDefinition = "varchar(50) comment '车辆原始类型'")
    private String vehicleOriginalType;

    @Column(name = "authorized_person_name",columnDefinition = "varchar(50) comment '授权人姓名'")
    private String authorizedPersonName;

    @Column(name = "authorized_person_phone",columnDefinition = "varchar(50) comment '授权人电话'")
    private String authorizedPersonPhone;

    @Column(name = "authorized_person_id_number",columnDefinition = "varchar(50) comment '授权人身份证号'")
    private String authorizedPersonIdNumber;

    @Column(name = "past_due_status",columnDefinition = "tinyint(4) comment '过期状态 1、已过期 0、未过期'")
    private Integer pastDueStatus;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间，也是实收时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;
}
