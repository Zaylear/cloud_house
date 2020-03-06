package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_space_info")
@Data
public class ParkingSpaceInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkingSpaceInfoId;

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

    @Column(name = "floor",columnDefinition = "varchar(10) comment '楼层 -1楼 用N1表示 1楼 用P1表示'")
    private String floor;

    @Column(name = "parking_space_place",columnDefinition = "varchar(100) comment '车位地点 1、地面 2、地下'")
    private Integer parkingSpacePlace;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "parking_space_nature",columnDefinition = "varchar(100) comment '车位性质 1、专有车位 2、租赁车位'")
    private String parkingSpaceNature;

    @Column(name = "parking_space_area",columnDefinition = "double comment '车位面积'")
    private Double parkingSpaceArea;

    @Column(name = "parking_space_type",columnDefinition = "varchar(100) comment '车位类型：子母车位、普通车位'")
    private String parkingSpaceType;

    @Column(name = "vehicle_capacity",columnDefinition = "int comment '车位容车数量，默认1'")
    private Integer vehicleCapacity;

    @Column(name = "current_capacity",columnDefinition = "int comment '当前可容车数量'")
    private Integer currentCapacity;

    @Column(name = "remarks",columnDefinition = "varchar(200) comment '备足'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
