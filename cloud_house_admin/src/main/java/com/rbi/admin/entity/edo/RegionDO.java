package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="regional_region")
@Data
public class RegionDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_code",columnDefinition = "varchar(20) comment '地块编号'")
    private String regionCode;

    @Column(name = "region_name",columnDefinition = "varchar(50) comment '地块名称'")
    private String regionName;

    @Column(name = "construction_area",columnDefinition = "double(20,0) comment '地块建筑面积'")
    private Double constructionArea;

    @Column(name = "greening_rate",columnDefinition = "double(20,0) comment '绿化率'")
    private Double greeningRate;

    @Column(name = "public_area",columnDefinition = "double(20,0) comment '公共场所面积'")
    private String publicArea;

    @Column(name = "village_code",columnDefinition = "varchar(20) comment '小区编号'")
    private String villageCode;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
