package com.rbi.entity.dto;

import java.io.Serializable;
import java.util.List;


public class PermitDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;


    private String permisCode;


    private String title;

    private Integer menuPermisFlag;

    private Integer permisOrder;

    private String router;

    private String color;

    private String parentCode;

    private String remark;

    private String idt;

    private String udt;

    List<?> permitDTO;

    public String getPermisCode() {
        return permisCode;
    }

    public void setPermisCode(String permisCode) {
        this.permisCode = permisCode;
    }

    public Integer getMenuPermisFlag() {
        return menuPermisFlag;
    }

    public void setMenuPermisFlag(Integer menuPermisFlag) {
        this.menuPermisFlag = menuPermisFlag;
    }

    public Integer getPermisOrder() {
        return permisOrder;
    }

    public void setPermisOrder(Integer permisOrder) {
        this.permisOrder = permisOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public List<?> getPermitDTO() {
        return permitDTO;
    }

    public void setPermitDTO(List<?> permitDTO) {
        this.permitDTO = permitDTO;
    }

    @Override
    public String toString() {
        return "PermitDTO{" +
                "id=" + id +
                ", permisCode='" + permisCode + '\'' +
                ", title='" + title + '\'' +
                ", menuPermisFlag=" + menuPermisFlag +
                ", permisOrder=" + permisOrder +
                ", router='" + router + '\'' +
                ", color='" + color + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", remark='" + remark + '\'' +
                ", idt='" + idt + '\'' +
                ", udt='" + udt + '\'' +
                ", permitDTO=" + permitDTO +
                '}';
    }
}
