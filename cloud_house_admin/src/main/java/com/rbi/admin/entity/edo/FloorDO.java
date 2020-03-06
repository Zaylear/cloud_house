package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="regional_floor")
public class FloorDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "floor_code",columnDefinition = "varchar(50) comment '楼层编号'")
    private String floorCode;

    @Column(name = "floor",columnDefinition = "int(4) comment '楼层数'")
    private Integer floor;

    @Column(name = "unit_code",columnDefinition = "varchar(50) comment '单元编号'")
    private String unitCode;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;


}
