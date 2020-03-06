package com.rbi.admin.entity.edo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "organization")
public class OrganizationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(50) comment '组织名称'")
    private String organizationName;

    @Column(name = "district_code",columnDefinition = "varchar(20) comment '县/区编号'")
    private String districtCode;

    @Column(name = "district_name",columnDefinition = "varchar(20) comment '县/区名称'")
    private String districtName;

    @Column(name = "open_id",columnDefinition = "varchar(100) comment '公司公众号openID'")
    private String openId;

    @Column(name = "postcode",columnDefinition = "varchar(50) comment '邮编'")
    private String postcode;

    @Column(name = "reg_no",columnDefinition = "varchar(50) comment '工商注册号'")
    private String regNo;

    @Column(name = "latitude",columnDefinition = "varchar(50) comment '纬度'")
    private String latitude;

    @Column(name = "longitude",columnDefinition = "varchar(50) comment '经度'")
    private String longitude;

    @Column(name = "legal_person",columnDefinition = "varchar(50) comment '法人'")
    private String legalPerson;

    @Column(name = "address",columnDefinition = "varchar(100) comment '公司地址'")
    private String address;

    @Column(name = "found_date",columnDefinition = "date comment '成立日期'")
    private String foundDate;

    @Column(name = "fax",columnDefinition = "varchar(50) comment '传真'")
    private String fax;

    @Column(name = "tel_number",columnDefinition = "varchar(50) comment '单位电话'")
    private String telNumber;

    @Column(name = "email",columnDefinition = "varchar(50) comment '邮箱'")
    private String email;

    @Column(name = "scale",columnDefinition = "varchar(50) comment '公司规模'")
    private String scale;

    @Column(name = "category",columnDefinition = "varchar(50) comment '公司类型'")
    private String category;

    @Column(name = "introduction",columnDefinition = "varchar(50) comment '简介'")
    private String introduction;

    @Column(name = "bill_name",columnDefinition = "varchar(50) comment '票据名称'")
    private String billName;

    @Column(name = "pid",columnDefinition = "varchar(50) comment '上级id'")
    private String pid;

    @Column(name = "pname",columnDefinition = "varchar(50) comment '上级名称'")
    private String pname;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

}
