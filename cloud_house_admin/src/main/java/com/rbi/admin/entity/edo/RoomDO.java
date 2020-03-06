package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
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

    @Column(name = "start_billing_time",columnDefinition = "date comment '开始计物业费时间'")
    private String startBillingTime;

    @Column(name = "real_recycling_home_time",columnDefinition = "date comment '实际收房日期'")
    private String realRecyclingHomeTime;

    @Column(name = "remarks",columnDefinition = "varchar(255) comment '备注'")
    private String remarks;

    @Column(name = "unit_code",columnDefinition = "varchar(50) comment '单元编号'")
    private String unitCode;

    @Column(name = "floor_code",columnDefinition = "varchar(50) comment '楼层'")
    private String floorCode;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
