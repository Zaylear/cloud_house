package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="event_record")
public class EventRecordDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(20) comment '组织id'")
    private String organizationId;

    @Column(name = "village_code",columnDefinition = "varchar(50) comment '小区编号'")
    private String villageCode;

    @Column(name = "event_code",columnDefinition = "varchar(20) comment '事件编号'")
    private String eventCode;

    @Column(name = "event_name",columnDefinition = "varchar(50) comment '事件名称'")
    private String eventName;

    @Column(name = "event_type",columnDefinition = "varchar(20) comment '事件类型 1、缴费提醒 2、退款提醒'")
    private String eventType;

    @Column(name = "event_descripte",columnDefinition = "text comment '事件描述'")
    private String eventDescripte;

    @Column(name = "process_state",columnDefinition = "tinyint(4) comment '处理状态（1 已处理 2未处理 3处理中 4过期）'")
    private Integer processState;

    @Column(name = "occur_time",columnDefinition = "datetime(0) comment '事件发生时间'")
    private String occurTime;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescripte() {
        return eventDescripte;
    }

    public void setEventDescripte(String eventDescripte) {
        this.eventDescripte = eventDescripte;
    }

    public Integer getProcessState() {
        return processState;
    }

    public void setProcessState(Integer processState) {
        this.processState = processState;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
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
