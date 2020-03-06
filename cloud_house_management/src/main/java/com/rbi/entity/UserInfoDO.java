package com.rbi.entity;

import javax.persistence.*;
import java.io.Serializable;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getEducationalBackground() {
        return educationalBackground;
    }

    public void setEducationalBackground(String educationalBackground) {
        this.educationalBackground = educationalBackground;
    }

    public Integer getWorkingYears() {
        return workingYears;
    }

    public void setWorkingYears(Integer workingYears) {
        this.workingYears = workingYears;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public Integer getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(Integer politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getVolk() {
        return volk;
    }

    public void setVolk(String volk) {
        this.volk = volk;
    }

    public String getTechnicalTitle() {
        return technicalTitle;
    }

    public void setTechnicalTitle(String technicalTitle) {
        this.technicalTitle = technicalTitle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
