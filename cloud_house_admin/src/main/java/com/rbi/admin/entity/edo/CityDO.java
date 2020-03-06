package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="regional_city")
public class CityDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city_code",columnDefinition = "varchar(20) comment '市编号'")
    private String cityCode;

    @Column(name = "city_name",columnDefinition = "varchar(20) comment '市名称'")
    private String cityName;

    @Column(name = "province_code",columnDefinition = "varchar(50) comment '省编号'")
    private String provinceCode;

    @Column(name = "province_name",columnDefinition = "varchar(50) comment '省名称'")
    private String provinceName;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
