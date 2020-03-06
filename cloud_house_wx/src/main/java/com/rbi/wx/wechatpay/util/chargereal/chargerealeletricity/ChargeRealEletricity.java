package com.rbi.wx.wechatpay.util.chargereal.chargerealeletricity;

import com.rbi.wx.wechatpay.mapper.ChargeRealMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.util.chargepayfactory.ChargePayDTO;
import com.rbi.wx.wechatpay.util.chargepayfactory.chargedAllDTO.ChargeElectricityDTO;
import com.rbi.wx.wechatpay.util.chargereal.ChargeReal;

import java.util.List;

public class ChargeRealEletricity implements ChargeReal {
    @Override
    public double getMoney(ChargePayRequestEntity chargePayRequestEntity, ChargeRealMapper chargeRealMapper) throws Exception {
        double standard=getStandard(chargeRealMapper.findElectricityByRoomCode(chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode()));
        double balanceAmount=0.0;
        if (chargePayRequestEntity.getCouponId()!=null&&!chargePayRequestEntity.getCouponId().equals("-1")){
            if (chargeRealMapper.couponIsTrue(chargePayRequestEntity.getCouponId(),chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode())==null){
                throw new Exception("无效的优惠券ID");
            }else {
                balanceAmount=chargeRealMapper.couponbalanceAmount(chargePayRequestEntity.getCouponId(),chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode());
            }
        }
        standard=-balanceAmount;
        standard=-chargeRealMapper.getSurplus(chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode());
        return standard;
    }
    private Double getStandard(List<ChargeElectricityDTO> list){
        Double standard=0.0;
        for (ChargeElectricityDTO chargeElectricityDTO:list){
            Double reaadNum=chargeElectricityDTO.getCurrentReadings()-chargeElectricityDTO.getLastReading();
            standard=+(reaadNum*chargeElectricityDTO.getChargeStandard());
        }
        return standard>=0.0?standard:0.0;
    }
}
