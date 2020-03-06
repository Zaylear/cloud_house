package com.rbi.admin.entity.edo;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_customer_info")
public class CustomerInfoDO implements Serializable {

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

    @Column(name = "sex",columnDefinition = "tinyint(4) comment '性别：1 男，2 女'")
    private String sex;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '姓氏'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '手机号码'")
    private String mobilePhone;

    @Column(name = "portrait_path",columnDefinition = "varchar(255) comment '头像路径'")
    private String portraitPath;

    @Column(name = "enabled",columnDefinition = "tinyint(4) DEFAULT '1' comment '是否可用: 1 可用，0 不可用'")
    private Integer enabled;

    @Column(name = "login_status",columnDefinition = "tinyint(4) DEFAULT '0' comment '登陆状态：1 已登录，0 未登录'")
    private Integer loginStatus;

    @Column(name = "remarks",columnDefinition = "varchar(255) comment '备注'")
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
