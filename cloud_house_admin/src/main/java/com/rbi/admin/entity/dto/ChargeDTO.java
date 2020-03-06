package com.rbi.admin.entity.dto;

import lombok.Data;

@Data
public class ChargeDTO {
    private String organizationId;//组织编号
    private String chargeCode;//收费项目
    private String chargeName;//项目名
    private Integer chargeType;//类型
    private String chargeUnit;//单位
    private Double chargeStandard;//单价
    private Integer chargeWay;//不显示  收费方式
    private Integer refund;//是否可退款
    private Integer mustPay;//是否可退款
    private Integer enable;//是否可用
    private Integer id;
    private Integer areaMin;//占地面积最大值
    private Integer areaMax;//占地面积最小值
    private Double money;//金额
    private Integer datedif;//月数
    private Double discount;//折扣
    private String parkingSpaceNature;//车位性质
    private String parkingSpaceType;//车位类型
    private String idt;
    private String udt;
}
