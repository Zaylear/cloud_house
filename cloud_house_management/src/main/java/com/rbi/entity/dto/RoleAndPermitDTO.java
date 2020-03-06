package com.rbi.entity.dto;

import java.io.Serializable;

public class RoleAndPermitDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    private Integer id;
    private String roleCode;
    private String roleName;

    private String permisCode;
    private String title;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermisCode() {
        return permisCode;
    }

    public void setPermisCode(String permisCode) {
        this.permisCode = permisCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
