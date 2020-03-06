package com.rbi.interactive.service.impl.report.summary;

public interface SummaryMethod {
    double propertyFee();//物业费
    double decorationManagementFee();//装修管理费
    double garbageCollectionAndTransportationFee();//垃圾清运费
    double decorationDeposit();//装修保证金/押金
    double electricityFees();//电费
    double waterFees();//水费
    double passCard();//出入证
    double accessCardFee();//门禁卡费
    double electricityMeterAccountOpeningFee();//电表开户费
    double naturalGasAccountOpeningFee();//天然气开户费
    double tvAccountOpeningFee();//电视开户费
    double totalAmountOfWriteOff(); //核销总额/减冲金额总额
    double totalOfThreeWayFees();//三通费合计
    double parkingSpaceRentalFee();//车位租赁费
    double parkingManagementFee(); //车位管理费
    double deductionThreeFees();//三通费抵扣金额
    double couponDeductionAmount();//优惠券抵扣金额
    double marginDeductionAmount();//保证金抵扣金额
    double correctedAmount();//修正金额
    double weChatPaymentAmount();//微信支付金额
    double alipayPaymentAmount();//支付宝支付金额
    double cashPaymentAmount();//现金支付金额
    double creditCardPaymentAmount();//刷卡支付金额
    double wechatPlatformPayment();//微信平台支付
    double totalTeceivables();//应收合计
    double totalActualRevenue();//实收合计
}
