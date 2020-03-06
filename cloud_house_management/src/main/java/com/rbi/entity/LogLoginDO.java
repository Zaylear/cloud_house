package com.rbi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "log_login")
public class LogLoginDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id",columnDefinition = "varchar(20) comment '用户ID，唯一标识，系统自动生成'")
    private String userId;

    @Column(name = "username",columnDefinition = "varchar(50) comment '用户名，唯一标识'")
    private String username;

    @Column(name = "real_name",columnDefinition = "varchar(50) comment '真实姓名'")
    private String realName;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '手机号码'")
    private String mobilePhone;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "ip_address",columnDefinition = "varchar(50) comment 'IP地址'")
    private String ipAddress;

    @Column(name = "event",columnDefinition = "varchar(50) comment '事件'")
    private String event;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "varchar(30) comment '更新时间'")
    private Long udt;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public Long getUdt() {
        return udt;
    }

    public void setUdt(Long udt) {
        this.udt = udt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
