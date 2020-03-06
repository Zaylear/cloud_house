package com.rbi.interactive.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="parking_space_statistics")
public class ParkingSpaceStatisticsDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "region_code",columnDefinition = "varchar(11) comment '地块编号'")
    private Integer regionCode;

    @Column(name = "region_number",columnDefinition = "int(11) DEFAULT '0' comment '地块车位总数量'")
    private Integer regionNumber;

    @Column(name = "region_proper_number",columnDefinition = "int(11) DEFAULT '0' comment '地块专有车位数量'")
    private Integer regionProperNumber;

    @Column(name = "region_rent_number",columnDefinition = "int(11) DEFAULT '0' comment '地块租赁车位数量'")
    private Integer regionRentNumber;

    @Column(name = "region_rented_number",columnDefinition = "int(11) DEFAULT '0' comment '地块租出去的车位数量'")
    private Integer regionRentedNumber;

    @Column(name = "not_rented_number",columnDefinition = "int(11) DEFAULT '0' comment '地块未租出去的车位数量'")
    private Integer notRentedNumber;

}
