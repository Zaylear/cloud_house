package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Table(name="department")
public class DepartmentDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(50) comment '组织/机构名'")
    private String organizationName;

    @Column(name = "dept_id",columnDefinition = "varchar(50) comment '部门id'")
    private String deptId;

    @Column(name = "dept_name",columnDefinition = "varchar(50) comment '部门名称'")
    private String deptName;

    @Column(name = "dept_category",columnDefinition = "varchar(50) comment '部门分类'")
    private String deptCategory;

    @Column(name = "tel_number",columnDefinition = "varchar(20) comment '电话号码'")
    private String telNumber;

    @Column(name = "fax",columnDefinition = "varchar(50) comment '传真'")
    private String fax;

    @Column(name = "description",columnDefinition = "varchar(200) comment '描述'")
    private String description;

    @Column(name = "pid",columnDefinition = "varchar(50) comment '父级编号'")
    private String pid;

    @Column(name = "pname",columnDefinition = "varchar(50) comment '父级名称'")
    private String pname;

    @Column(name = "flag",columnDefinition = "int(4) comment '节点层级'")
    private Integer flag;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
