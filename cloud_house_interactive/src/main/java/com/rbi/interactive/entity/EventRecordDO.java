package com.rbi.interactive.entity;


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
}
