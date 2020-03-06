package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "parking_space_cost_detail")
public class ParkingSpaceCostDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkingSpaceCostDetailId;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号'")
    private String chargeCode;

    @Column(name = "charge_name",columnDefinition = "varchar(80) comment '项目名称'")
    private String chargeName;

    @Column(name = "charge_type",columnDefinition = "varchar(10) comment '收费类型 1、物业费 2、......'")
    private String chargeType;

    @Column(name = "charge_standard",columnDefinition = "double(20,4) comment '收费标准单价'")
    private Double chargeStandard;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "datedif",columnDefinition = "int(11) comment '缴费月数'")
    private Integer datedif;

    @Column(name = "discount",columnDefinition = "double(20,4) comment '折扣'")
    private Double discount;

    @Column(name = "parking_space_place",columnDefinition = "varchar(100) comment '车位地点 1、地面 2、地下'")
    private Integer parkingSpacePlace;

    @Column(name = "parking_space_type",columnDefinition = "varchar(100) comment '车位类型：子母车位'")
    private String parkingSpaceType;

    @Column(name = "amount_receivable",columnDefinition = "double(20,4) comment '应收金额'",scale = 2)
    private Double amountReceivable;

    @Column(name = "actual_money_collection",columnDefinition = "double(20,4) comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "contract_number",columnDefinition = "varchar(100) comment '合同编号'")
    private String contractNumber;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "rental_renewal_status",columnDefinition = "tinyint(4) comment '续租状态 1、续租 0、非续租'")
    private Integer rentalRenewalStatus;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "authorized_person_name",columnDefinition = "varchar(50) comment '车主姓名'")
    private String authorizedPersonName;

    @Column(name = "authorized_person_phone",columnDefinition = "varchar(50) comment '车主电话'")
    private String authorizedPersonPhone;

    @Column(name = "authorized_person_id_number",columnDefinition = "varchar(50) comment '车主身份证号'")
    private String authorizedPersonIdNumber;

    @Column(name = "surname",columnDefinition = "varchar(4) comment '姓名'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "license_plate_number",columnDefinition = "varchar(50) comment '车牌号'")
    private String licensePlateNumber;

    @Column(name = "license_plate_color",columnDefinition = "varchar(50) comment '车牌颜色'")
    private String licensePlateColor;

    @Column(name = "license_plate_type",columnDefinition = "varchar(50) comment '车牌类型 '")
    private String licensePlateType;

    @Column(name = "vehicle_original_type",columnDefinition = "varchar(50) comment '车辆原始类型'")
    private String vehicleOriginalType;

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

    @Column(name = "floor",columnDefinition = "varchar(10) comment '楼层 -1楼 用(-1)表示 1楼 用(1)表示'")
    private String floor;
}
