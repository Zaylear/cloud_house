package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_user_info")
public class UserInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id",columnDefinition = "varchar(20) comment '用户ID，唯一标识，系统自动生成'")
    private String userId;

    @Column(name = "username",columnDefinition = "varchar(50) comment '用户名，唯一标识'")
    private String username;

    @Column(name = "password",columnDefinition = "varchar(255) comment '密码'")
    private String password;

    @Column(name = "salt",columnDefinition = "varchar(255) comment '盐'")
    private String salt;

    @Column(name = "real_name",columnDefinition = "varchar(50) comment '真实姓名'")
    private String realName;

    @Column(name = "sex",columnDefinition = "varchar(4) comment '性别'")
    private String sex;

    @Column(name = "birthday",columnDefinition = "varchar(20) comment '出生日期'")
    private String birthday;

    @Column(name = "email",columnDefinition = "varchar(50) comment 'E-mail'")
    private String email;

    @Column(name = "address",columnDefinition = "varchar(255) comment '地址'")
    private String address;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '手机号码'")
    private String mobilePhone;

    @Column(name = "enabled",columnDefinition = "tinyint(4) DEFAULT '1' comment '是否可用：1 可用，0 不可用'")
    private Integer enabled;

    @Column(name = "identity",columnDefinition = "varchar(18) comment '身份证号'")
    private String identity;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(50) comment '组织/机构名'")
    private String organizationName;

    @Column(name = "department_id",columnDefinition = "varchar(40) comment '部门ID'")
    private String departmentId;

    @Column(name = "department_name",columnDefinition = "varchar(40) comment '部门名'")
    private String departmentName;

    @Column(name = "portrait_path",columnDefinition = "varchar(255) comment '头像路径'")
    private String portraitPath;

    @Column(name = "login_status",columnDefinition = "tinyint(4) DEFAULT '0' comment '登陆状态：1 已登录，0 未登录'")
    private Integer loginStatus;

    @Column(name = "educational_background",columnDefinition = "varchar(20) comment '学历'")
    private String educationalBackground;

    @Column(name = "working_years",columnDefinition = "int(4) comment '工龄'")
    private Integer workingYears;

    @Column(name = "hiredate",columnDefinition = "datetime(0) comment '入职时间'")
    private String hiredate;

    @Column(name = "native_place",columnDefinition = "varchar(30) comment '籍贯'")
    private String nativePlace;

    @Column(name = "political_status",columnDefinition = "int(4) comment '政治面貌'")
    private Integer politicalStatus;

    @Column(name = "marital_status",columnDefinition = "int(4) comment '婚姻状况'")
    private Integer maritalStatus;

    @Column(name = "volk",columnDefinition = "varchar(30) comment '名族'")
    private String volk;

    @Column(name = "technical_title",columnDefinition = "varchar(30) comment '职称'")
    private String technicalTitle;

    @Column(name = "theme",columnDefinition = "varchar(30) DEFAULT 'green' comment '主题 1绿（默认） 2蓝 3粉 4棕 5黑'")
    private String theme;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;


}
