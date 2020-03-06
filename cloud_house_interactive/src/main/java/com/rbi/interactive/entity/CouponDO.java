package com.rbi.interactive.entity;



import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="pay_coupon")
@Data
public class CouponDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "coupon_code",columnDefinition = "varchar(20) comment '优惠卷编号'")
    private String couponCode;

    @Column(name = "coupon_name",columnDefinition = "varchar(20) comment '优惠卷名称'")
    private String couponName;

    @Column(name = "coupon_type",columnDefinition = "varchar(20) comment '优惠卷类型'")
    private String couponType;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号,微信端识别物业费收费项目'")
    private String chargeCode;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织id'")
    private String organizationId;

    @Column(name = "distributor",columnDefinition = "varchar(20) comment '发放人'")
    private String distributor;

    @Column(name = "money",columnDefinition = "double(20,0) comment '金额'")
    private Double money;

    @Column(name = "enable",columnDefinition = "int(4) comment '发放人'")
    private Integer enable;

    @Column(name = "effective_time",columnDefinition = "int(4) comment '有效时长 0:永久 1:x天 2:xx天'")
    private Integer effectiveTime;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'优惠卷制作时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;
}
