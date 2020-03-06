package com.rbi.wx.wechatpay.util.chargepayfactory;

public class ChargeTypeFactory {
    public static ChargePayFactoryInterface getChargePay(Integer chargeType){
        switch (chargeType){
            case 1:return new ChargePayProperty();
            case 7:return new ChargePayElectricity();
            default:return null;
        }
    }
}
