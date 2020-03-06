package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report")
@Data
public class ReportDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code",columnDefinition = "varchar(50) comment '编号'")
    private String code;

    @Column(name = "title",columnDefinition = "varchar(100) comment '名称'")
    private String title;

    @Column(name = "parent_code",columnDefinition = "varchar(50) comment '父级编号 0、报表名称'")
    private String parentCode;

    @Column(name = "param_type",columnDefinition = "int comment '1、查询参数 2、条件参数'")
    private Integer paramType;

    @Column(name = "status",columnDefinition = "int comment '1、输入框 2、下拉框 3、时间日期选择 4、'")
    private Integer status;

    @Column(name = "sort_number",columnDefinition = "int comment '排序编号'")
    private Integer sortNumber;

    @Column(name = "idt",columnDefinition = "datetime comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime comment '更新时间'")
    private String udt;
}
