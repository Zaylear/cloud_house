package com.rbi.interactive.entity;



import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="charge_item")
public class ChargeItemDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号'")
    private String chargeCode;

    @Column(name = "charge_name",columnDefinition = "varchar(20) comment '项目名称'")
    private String chargeName;

    @Column(name = "charge_type",columnDefinition = "int(20) comment '项目类型：1、物业费 2、装修管理费 3、垃圾清运费 4、装修保证金  后续'")
    private Integer chargeType;

    @Column(name = "sort_state",columnDefinition = "int(20) default 0 comment '排序状态 升序'")
    private Integer sortState;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "charge_standard",columnDefinition = "double(20,2) comment '收费单价'")
    private Double chargeStandard;

    @Column(name = "charge_way",columnDefinition = "tinyint(4) comment '缴费方式 1、按月 2、其他'")
    private Integer chargeWay;

    @Column(name = "must_pay",columnDefinition = "int(4) comment '是否必缴费用 1、是  2、否'")
    private Integer mustPay;

    @Column(name = "refund",columnDefinition = "tinyint(4) comment '是否可退款 1、是 2、否'")
    private Integer refund;

    @Column(name = "enable",columnDefinition = "int(11) comment '是否可用：1、可用 2、不可用'")
    private Integer enable;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'插入时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
