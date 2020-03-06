package com.rbi.admin.entity.edo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "log_owner_information")
public class LogOwnerInformationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code",columnDefinition = "int comment '唯一标识'")
    private Integer code;

    @Column(name = "room_code",columnDefinition = "varchar(100) comment '房间编号'")
    private String roomCode;

    @Column(name = "result",columnDefinition = "varchar(100) comment '导入结果'")
    private String result;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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
