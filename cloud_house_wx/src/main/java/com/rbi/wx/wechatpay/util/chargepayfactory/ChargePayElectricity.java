package com.rbi.wx.wechatpay.util.chargepayfactory;

import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.util.chargepayfactory.chargedAllDTO.ChargeElectricityDTO;

import java.util.List;

public class ChargePayElectricity implements ChargePayFactoryInterface{
    @Override
    public ChargePayDTO getChargePayDTO(ChargePayRequestEntity chargePayRequestEntity,PayMentMapper payMentMapper) {
        double standard=getStandard(payMentMapper.findElectricityByRoomCode(chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode()));
        ChargePayDTO chargePayDTO=new ChargePayDTO();
        chargePayDTO.setDatedif("false");
        chargePayDTO.setRoomCode(chargePayRequestEntity.getRoomCode());
        chargePayDTO.setMoney("false");
        chargePayDTO.setNewMoney(standard+"");
        if (chargePayRequestEntity.getCouponId()==null||chargePayRequestEntity.getCouponId().equals("-1")){
            chargePayDTO.setCouponMoney("null");
            chargePayDTO.setNewMoney(standard+"");
        }else {
            double couponMoney=payMentMapper.getCouponMoney(chargePayRequestEntity.getCouponId());
            chargePayDTO.setCouponMoney(couponMoney+"");
            chargePayDTO.setNewMoney((standard-couponMoney)+"");
        }
        Double surplus=payMentMapper.getSurplus(chargePayDTO.getRoomCode(),chargePayRequestEntity.getOpenId());
        chargePayDTO.setNewMoney((Double.valueOf(chargePayDTO.getNewMoney())-surplus)+"");
        return chargePayDTO;
    }
    private Double getStandard(List<ChargeElectricityDTO> list){
        Double standard=0.0;
        for (ChargeElectricityDTO chargeElectricityDTO:list){
            Double reaadNum=chargeElectricityDTO.getCurrentReadings()-chargeElectricityDTO.getLastReading();
            standard=+(reaadNum*chargeElectricityDTO.getChargeStandard());
        }
        return standard;
    }
}
