package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "regional_divison")
public class DivisonDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "divison_code",columnDefinition = "varchar(50) comment '编号'")
    private String divisonCode;

    @Column(name = "divison_name",columnDefinition = "varchar(50) comment '名称'")
    private String divisonName;

    @Column(name = "pid",columnDefinition = "varchar(50) comment '上级编号'")
    private String pid;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

}
