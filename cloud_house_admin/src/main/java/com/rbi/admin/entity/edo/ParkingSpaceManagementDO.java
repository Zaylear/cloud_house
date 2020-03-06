package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_space_management")
@Data
public class ParkingSpaceManagementDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkingSpaceManagementId;

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

    @Column(name = "surname",columnDefinition = "varchar(4) comment '姓名'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

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

    @Column(name = "contract_number",columnDefinition = "varchar(100) comment '合同编号'")
    private String contractNumber;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "authorized_person_name",columnDefinition = "varchar(50) comment '授权人姓名'")
    private String authorizedPersonName;

    @Column(name = "authorized_person_phone",columnDefinition = "varchar(50) comment '授权人电话'")
    private String authorizedPersonPhone;

    @Column(name = "authorized_person_id_number",columnDefinition = "varchar(50) comment '授权人身份证号'")
    private String authorizedPersonIdNumber;

    @Column(name = "license_plate_number",columnDefinition = "varchar(50) comment '车牌号'")
    private String licensePlateNumber;

    @Column(name = "license_plate_color",columnDefinition = "varchar(50) comment '车牌颜色'")
    private String licensePlateColor;

    @Column(name = "license_plate_type",columnDefinition = "varchar(50) comment '车牌类型 '")
    private String licensePlateType;

    @Column(name = "vehicle_original_type",columnDefinition = "varchar(50) comment '车辆原始类型'")
    private String vehicleOriginalType;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "logged_off_state",columnDefinition = "tinyint(4) comment '注销状态：1、已注销 0、未注销 指注销专有车位'")
    private Integer loggedOffState;

    @Column(name = "remarks",columnDefinition = "varchar(200) comment '备足'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
