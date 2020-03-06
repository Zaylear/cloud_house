package com.rbi.interactive.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="regional_village")
public class VillageDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "village_code",columnDefinition = "varchar(20) comment '小区编号'")
    private String villageCode;

    @Column(name = "village_name",columnDefinition = "varchar(50) comment '小区名称'")
    private String villageName;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(50) comment '组织/机构名'")
    private String organizationName;

    @Column(name = "district_code",columnDefinition = "varchar(20) comment '县/区编号'")
    private String districtCode;

    @Column(name = "district_name",columnDefinition = "varchar(20) comment '县/区名称'")
    private String districtName;

    @Column(name = "construction_area",columnDefinition = "double(20,0) comment '总建筑面积'")
    private Double constructionArea;

    @Column(name = "greening_rate",columnDefinition = "double(20,0) comment '绿化率'")
    private Double greeningRate;

    @Column(name = "public_area",columnDefinition = "double(20,0) comment '公共场所面积'")
    private Double publicArea;

    @Column(name = "enable",columnDefinition = "int(11) comment '是否可用'")
    private Integer enable;

    @Column(name = "picture_path",columnDefinition = "varchar(100) comment '图片路径'")
    private String picturePath;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
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

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Double getConstructionArea() {
        return constructionArea;
    }

    public void setConstructionArea(Double constructionArea) {
        this.constructionArea = constructionArea;
    }

    public Double getGreeningRate() {
        return greeningRate;
    }

    public void setGreeningRate(Double greeningRate) {
        this.greeningRate = greeningRate;
    }

    public Double getPublicArea() {
        return publicArea;
    }

    public void setPublicArea(Double publicArea) {
        this.publicArea = publicArea;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
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

    @Override
    public String toString() {
        return "VillageDO{" +
                "id=" + id +
                ", villageCode='" + villageCode + '\'' +
                ", villageName='" + villageName + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", districtName='" + districtName + '\'' +
                ", constructionArea=" + constructionArea +
                ", greeningRate=" + greeningRate +
                ", publicArea=" + publicArea +
                ", enable=" + enable +
                ", picturePath='" + picturePath + '\'' +
                ", idt='" + idt + '\'' +
                ", udt='" + udt + '\'' +
                '}';
    }
}
