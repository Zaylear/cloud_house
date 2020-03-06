package com.rbi.interactive.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "collection_summary")
@Data
public class CollectionSummaryDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer collectionSummaryId;

    @Column(name = "statistics_time",columnDefinition = "date comment '统计时间'")
    private String statisticsTime;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "village_code",columnDefinition = "varchar(50) comment '小区编号'")
    private String villageCode;

    @Column(name = "village_name",columnDefinition = "varchar(50) comment '小区名称'")
    private String villageName;

    @Column(name = "region_code",columnDefinition = "varchar(20) comment '地块编号'")
    private String regionCode;

    @Column(name = "region_name",columnDefinition = "varchar(50) comment '地块名称'")
    private String regionName;

    @Column(name = "building_code",columnDefinition = "varchar(20) comment '楼宇编号'")
    private String buildingCode;

    @Column(name = "Building_name",columnDefinition = "varchar(50) comment '楼宇名称'")
    private String buildingName;

    @Column(name = "unit_code",columnDefinition = "varchar(20) comment '单元编号'")
    private String unitCode;

    @Column(name = "unit_name",columnDefinition = "varchar(20) comment '单元名称'")
    private String unitName;

    @Column(name = "room_code",columnDefinition = "varchar(100) comment '房间编号'")
    private String roomCode;

    @Column(name = "room_type",columnDefinition = "int(4) comment '房屋类型：0、民宿 1、商用'")
    private Integer roomType;

    @Column(name = "room_size",columnDefinition = "double(16,4) comment '房间面积'")
    private Double roomSize;

    @Column(name = "surname",columnDefinition = "varchar(50) comment '客户姓名'")
    private String surname;

    @Column(name = "id_number",columnDefinition = "varchar(18) comment '身份证号'")
    private String idNumber;

    @Column(name = "mobile_phone",columnDefinition = "varchar(100) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "due_time",columnDefinition = "date comment '物业费到期时间'")
    private String dueTime;//物业费到期时间

    @Column(name = "property_fee",columnDefinition = "double comment '物业费实收金额'",scale = 1)
    private Double propertyFee;//物业费

    @Column(name = "decoration_management_fee",columnDefinition = "double comment '装修管理费'",scale = 1)
    private Double decorationManagementFee;//装修管理费

    @Column(name = "garbage_collection_and_transportation_fee",columnDefinition = "double comment '垃圾清运费'",scale = 1)
    private Double garbageCollectionAndTransportationFee;//垃圾清运费

    @Column(name = "decoration_deposit",columnDefinition = "double comment '装修保证金/押金'",scale = 1)
    private Double decorationDeposit;//装修保证金/押金

    @Column(name = "electricity_fees",columnDefinition = "double comment '电费'",scale = 1)
    private Double electricityFees;//电费

    @Column(name = "water_fees",columnDefinition = "double comment '水费'",scale = 1)
    private Double waterFees;//水费

    @Column(name = "pass_card",columnDefinition = "double comment '出入证'",scale = 1)
    private Double passCard;//出入证

    @Column(name = "access_card_fee",columnDefinition = "double comment '门禁卡费'",scale = 1)
    private Double accessCardFee;//门禁卡费

    @Column(name = "electricity_meter_account_opening_fee",columnDefinition = "double comment '电表开户费'",scale = 1)
    private Double electricityMeterAccountOpeningFee;//电表开户费

    @Column(name = "natural_gas_account_opening_fee",columnDefinition = "double comment '天然气开户费'",scale = 1)
    private Double naturalGasAccountOpeningFee;//天然气开户费

    @Column(name = "tv_account_opening_fee",columnDefinition = "double comment '电视开户费'",scale = 1)
    private Double tvAccountOpeningFee;//电视开户费

    @Column(name = "total_amount_of_write_off",columnDefinition = "double comment '核销总额/减冲金额总额'",scale = 1)
    private Double totalAmountOfWriteOff; //核销总额/减冲金额总额

    @Column(name = "total_of_three_way_fees",columnDefinition = "double comment '三通费合计'",scale = 1)
    private Double totalOfThreeWayFees;//三通费合计

    @Column(name = "parking_space_rental_fee",columnDefinition = "double comment '车位租赁费'",scale = 1)
    private Double parkingSpaceRentalFee;//车位租赁费

    @Column(name = "parking_management_fee",columnDefinition = "double comment '车位管理费'",scale = 1)
    private Double parkingManagementFee; //车位管理费

    @Column(name = "parking_space_number",columnDefinition = "double comment '车位编号'",scale = 1)
    private String parkingSpaceNumber;//车位编号

    @Column(name = "license_plate_number",columnDefinition = "double comment '车牌号'",scale = 1)
    private String licensePlateNumber;//车牌号

    @Column(name = "authorized_person_name",columnDefinition = "double comment '车主姓名'",scale = 1)
    private String authorizedPersonName;//车主姓名

    @Column(name = "authorized_person_phone",columnDefinition = "double comment '车主电话'",scale = 1)
    private String authorizedPersonPhone;//车主电话

    @Column(name = "deduction_three_fees",columnDefinition = "double comment '三通费抵扣金额'",scale = 1)
    private Double deductionThreeFees;//三通费抵扣金额

    @Column(name = "coupon_deduction_amount",columnDefinition = "double comment '优惠券抵扣金额'",scale = 1)
    private Double couponDeductionAmount;//优惠券抵扣金额

    @Column(name = "margin_deduction_amount",columnDefinition = "double comment '保证金抵扣金额'",scale = 1)
    private Double marginDeductionAmount;//保证金抵扣金额

    @Column(name = "corrected_amount",columnDefinition = "double comment '修正金额'",scale = 1)
    private Double correctedAmount;//修正金额

    @Column(name = "we_chat_payment_amount",columnDefinition = "double comment '微信支付金额'",scale = 1)
    private Double weChatPaymentAmount;//微信支付金额

    @Column(name = "alipay_payment_amount",columnDefinition = "double comment '支付宝支付金额'",scale = 1)
    private Double alipayPaymentAmount;//支付宝支付金额

    @Column(name = "cash_payment_amount",columnDefinition = "double comment '现金支付金额'",scale = 1)
    private Double cashPaymentAmount;//现金支付金额

    @Column(name = "credit_card_payment_amount",columnDefinition = "double comment '刷卡支付金额'",scale = 1)
    private Double creditCardPaymentAmount;//刷卡支付金额

    @Column(name = "wechat_platform_payment",columnDefinition = "double comment '微信平台支付'",scale = 1)
    private Double wechatPlatformPayment;//微信平台支付

    @Column(name = "total_teceivables",columnDefinition = "double comment '应收合计'",scale = 1)
    private Double totalTeceivables;//应收合计

    @Column(name = "total_actual_revenue",columnDefinition = "double comment '实收合计'",scale = 1)
    private Double totalActualRevenue;//实收合计

    @Column(name = "property_fee_billing_period",columnDefinition = "varchar(100) comment '物业费计费期间'",scale = 1)
    private String propertyFeeBillingPeriod;//物业费计费期间


}