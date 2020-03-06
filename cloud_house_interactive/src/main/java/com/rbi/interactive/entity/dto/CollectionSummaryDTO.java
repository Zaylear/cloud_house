package com.rbi.interactive.entity.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CollectionSummaryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String statisticsTime;

    private String regionCode;
    private String regionName;
    private String buildingCode;
    private String buildingName;
    private String unitCode;
    private String unitName;

    private String roomCode;
    private String roomType;
    private Double roomSize;

    private String surname;
    private String mobilePhone;
    private String dueTime;

    private Double propertyFee;//物业费
    private Double decorationManagementFee;//装修管理费
    private Double garbageCollectionAndTransportationFee;//垃圾清运费
    private Double decorationDeposit;//装修保证金/押金
    private Double electricityFees;//电费
    private Double waterFees;//水费
    private Double passCard;//出入证
    private Double accessCardFee;//门禁卡费
    private Double electricityMeterAccountOpeningFee;//电表开户费
    private Double naturalGasAccountOpeningFee;//天然气开户费
    private Double tvAccountOpeningFee;//电视开户费
    private Double totalAmountOfWriteOff; //核销总额/减冲金额总额
    private Double totalOfThreeWayFees;//三通费合计

    private Double parkingSpaceRentalFee;//车位租赁费
    private Double parkingManagementFee; //车位管理费
    private String parkingSpaceNumber;//车位编号
    private String licensePlateNumber;//车牌号
    private String authorizedPersonName;//车主姓名
    private String authorizedPersonPhone;//车主电话

    private Double deductionThreeFees;//三通费抵扣金额
    private Double couponDeductionAmount;//优惠券抵扣金额
    private Double marginDeductionAmount;//保证金抵扣金额
    private Double correctedAmount;//修正金额

    private Double weChatPaymentAmount;//微信支付金额
    private Double alipayPaymentAmount;//支付宝支付金额
    private Double cashPaymentAmount;//现金支付金额
    private Double creditCardPaymentAmount;//刷卡支付金额
    private Double wechatPlatformPayment;//微信平台支付

    private Double totalTeceivables;//应收合计
    private Double totalActualRevenue;//实收合计

    private String propertyFeeBillingPeriod;//物业费计费期间
}
