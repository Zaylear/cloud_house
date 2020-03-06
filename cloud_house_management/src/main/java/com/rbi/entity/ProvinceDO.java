package com.rbi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "regional_province")
public class ProvinceDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "province_code",columnDefinition = "varchar(50) comment '省编号'")
    private String provinceCode;

    @Column(name = "province_name",columnDefinition = "varchar(50) comment '省名称'")
    private String provinceName;

    @Column(name = "parent_code",columnDefinition = "varchar(50) comment '父级编号'")
    private String parentCode;

    @Column(name = "idt",columnDefinition = "varchar(50) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "varchar(50) comment '更新时间'")
    private String udt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
