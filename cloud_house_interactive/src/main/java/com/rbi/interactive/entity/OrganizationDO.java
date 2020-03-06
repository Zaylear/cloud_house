package com.rbi.interactive.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "organization")
public class OrganizationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "open_id",columnDefinition = "varchar(100) comment '公司公众号openID'")
    private String openId;

    @Column(name = "organization_name",columnDefinition = "varchar(50) comment '组织名称'")
    private String organizationName;

    @Column(name = "district_code",columnDefinition = "varchar(20) comment '县/区编号'")
    private String districtCode;

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

    @Column(name = "found_date",columnDefinition = "datetime(0) comment '成立日期'")
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

    @Column(name = "pid",columnDefinition = "varchar(50)  DEFAULT '0' comment '上级节点ID：1总  其他：选入组织id'")
    private String pid;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }
}
