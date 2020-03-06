package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "exclusive_parking_space_history_data_due_time")
public class ExclusiveParkingSpaceHistoryDataDueTimeDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "organization_id",columnDefinition = "varchar(20) comment '组织id'")
    private String organizationId;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
