package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="regional_district")
public class DistrictDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "district_code",columnDefinition = "varchar(20) comment '县/区编号'")
    private String districtCode;

    @Column(name = "district_name",columnDefinition = "varchar(20) comment '县/区名称'")
    private String districtName;

    @Column(name = "city_code",columnDefinition = "varchar(20) comment '市编号'")
    private String cityCode;

    @Column(name = "city_name",columnDefinition = "varchar(20) comment '市名称'")
    private String cityName;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
