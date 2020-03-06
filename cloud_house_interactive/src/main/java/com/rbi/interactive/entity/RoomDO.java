package com.rbi.interactive.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="regional_room")
public class RoomDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_code",columnDefinition = "varchar(50) comment '房间编号'")
    private String roomCode;

    @Column(name = "room_size",columnDefinition = "double(20,2) comment '住房大小 单位：平方米'")
    private Double roomSize;

    @Column(name = "room_type",columnDefinition = "int(4) comment '房屋类型：0、民宿 1、商用'")
    private Integer roomType;

    @Column(name = "room_status",columnDefinition = "int(4) comment '房间状态：0、未知 1、使用中 2、空置房'")
    private Integer roomStatus;

    @Column(name = "renovation_status",columnDefinition = "int(4) comment '装修情况：0、未装修 1、已装修'")
    private Integer renovationStatus;

    @Column(name = "renovation_start_time",columnDefinition = "date comment '装修开始时间'")
    private String renovationStartTime;

    @Column(name = "renovation_deadline",columnDefinition = "date comment '装修结束时间'")
    private String renovationDeadline;

    @Column(name = "remarks",columnDefinition = "varchar(255) comment '备注'")
    private String remarks;

    @Column(name = "unit_code",columnDefinition = "varchar(20) comment '单元编号'")
    private String unitCode;

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

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Integer getRenovationStatus() {
        return renovationStatus;
    }

    public void setRenovationStatus(Integer renovationStatus) {
        this.renovationStatus = renovationStatus;
    }

    public String getRenovationStartTime() {
        return renovationStartTime;
    }

    public void setRenovationStartTime(String renovationStartTime) {
        this.renovationStartTime = renovationStartTime;
    }

    public String getRenovationDeadline() {
        return renovationDeadline;
    }

    public void setRenovationDeadline(String renovationDeadline) {
        this.renovationDeadline = renovationDeadline;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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
