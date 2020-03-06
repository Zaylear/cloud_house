package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "exclusive_parking_space_history_data")
public class ExclusiveParkingSpaceHistoryDataDO implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parking_space_code",columnDefinition = "varchar(50) comment '车位编号'")
    private String parkingSpaceCode;

    @Column(name = "delivery_time",columnDefinition = "date comment '房开交付物业时间'")
    private String deliveryTime;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "amount_receivable",columnDefinition = "double(20,4) comment '应收金额'",scale = 2)
    private Double amountReceivable;

    @Column(name = "actual_money_collection",columnDefinition = "double(20,4) comment '实收金额'",scale = 2)
    private Double actualMoneyCollection;

    @Column(name = "vacancy_charge",columnDefinition = "double(20,2) comment '空置费'")
    private Double vacancyCharge;

    @Column(name = "remarks",columnDefinition = "text comment '备注'")
    private String remarks;

    @Column(name = "organization_id",columnDefinition = "varchar(20) comment '组织id'")
    private String organizationId;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
