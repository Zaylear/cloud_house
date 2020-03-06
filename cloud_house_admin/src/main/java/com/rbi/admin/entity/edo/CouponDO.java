package com.rbi.admin.entity.edo;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="pay_coupon")
public class CouponDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "coupon_code",columnDefinition = "varchar(50) comment '优惠卷编号'")
    private String couponCode;

    @Column(name = "coupon_type",columnDefinition = "varchar(50) comment '优惠卷类型'")
    private String couponType;

    @Column(name = "coupon_name",columnDefinition = "varchar(50) comment '优惠卷名称'")
    private String couponName;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织id'")
    private String organizationId;

    @Column(name = "distributor",columnDefinition = "varchar(50) comment '发放人'")
    private String distributor;

    @Column(name = "money",columnDefinition = "double(20,2) comment '金额'")
    private Double money;

    @Column(name = "effective_time",columnDefinition = "int(11) comment '有效时长 0:永久 1:x天 2:xx天'")
    private Integer effectiveTime;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '收费项目编号'")
    private String chargeCode;

    @Column(name = "enable",columnDefinition = "int(4) comment '是否可用'")
    private Integer enable;

    @Column(name = "idt",columnDefinition = "datetime(0) comment'优惠卷制作时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment'修改时间'")
    private String udt;

}
