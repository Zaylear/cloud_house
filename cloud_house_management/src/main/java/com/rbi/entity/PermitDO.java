package com.rbi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_permit")
public class PermitDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "permis_code",columnDefinition = "varchar(50) comment '权限代码'")
    private String permisCode;

    @Column(name = "title",columnDefinition = "varchar(100) comment '权限名称'")
    private String title;

    @Column(name = "menu_permis_flag",columnDefinition = "tinyint(4) comment '菜单类型：-1-只有红鸟管理端才可查看的权限；0-系统权限; 1-菜单权限；2-功能权限'")
    private Integer menuPermisFlag;

    @Column(name = "permis_order",columnDefinition = "boolean comment '模块：0、子系统 1、一级菜单 2、二级菜单 3、三级菜单 4、四级菜单 10、按钮'")
    private Integer permisOrder;

    @Column(name = "router",columnDefinition = "varchar(150) comment '路由'")
    private String router;

    @Column(name = "color",columnDefinition = "varchar(50) comment '颜色'")
    private String color;

    @Column(name = "parent_code",columnDefinition = "varchar(50) comment '父级权限编号'")
    private String parentCode;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

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

    @Override
    public String toString() {
        return "PermitDO{" +
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
                '}';
    }
}
