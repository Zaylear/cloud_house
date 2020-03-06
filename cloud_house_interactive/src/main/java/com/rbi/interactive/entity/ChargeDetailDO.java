package com.rbi.interactive.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="charge_detail")
public class ChargeDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "charge_code",columnDefinition = "varchar(100) comment '项目编号'")
    private String chargeCode;

    @Column(name = "area_min",columnDefinition = "double(10,2) comment '面积最小值'")
    private Double areaMin;

    @Column(name = "area_max",columnDefinition = "double(10,2) comment '面积最大值'")
    private Double areaMax;

    @Column(name = "money",columnDefinition = "double(10,2) comment '金额'")
    private Double money;

    @Column(name = "datedif",columnDefinition = "int(11) comment '缴费月数'")
    private Integer datedif;

    @Column(name = "discount",columnDefinition = "double(10,2) comment '折扣'")
    private Double discount;

    @Column(name = "parking_space_place",columnDefinition = "varchar(50) comment '车位地点 地面 地下'")
    private String parkingSpacePlace;

    @Column(name = "parking_space_type",columnDefinition = "varchar(50) comment '车位类型 1、专有车位 租赁车位'")
    private String parkingSpaceType;

}
