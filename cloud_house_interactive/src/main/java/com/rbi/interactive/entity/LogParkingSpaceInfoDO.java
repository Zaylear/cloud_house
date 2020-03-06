package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "log_parking_space_info")
public class LogParkingSpaceInfoDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code",columnDefinition = "int comment '表格序号'")
    private Integer code;

    @Column(name = "parking_space_code",columnDefinition = "varchar(100) comment '车位编号'")
    private String parkingSpaceCodes;

    @Column(name = "result",columnDefinition = "varchar(100) comment '导入结果'")
    private String result;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;
}
