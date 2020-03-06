package com.rbi.wx.wechatpay.util.chargereal;

import com.rbi.wx.wechatpay.util.chargereal.chargerealeletricity.ChargeRealEletricity;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.ChargeRealProperty;

public class ChargeRealFactory {
    public static ChargeReal getChargeReal(Integer chargeType){
        if (chargeType==1){
            return new ChargeRealProperty();
        }else if (chargeType==7){
            return new ChargeRealEletricity();
        }
        else {
            return null;
        }
    }
}
