package com.rbi.wx.wechatpay.util.chargereal.chargerealproperty;

import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.util.chargereal.ChargeReal;
import com.rbi.wx.wechatpay.mapper.ChargeRealMapper;
import sun.awt.geom.AreaOp;

import java.text.DecimalFormat;


public class ChargeRealProperty implements ChargeReal {

    public double getMoney(ChargePayRequestEntity chargePayRequestEntity, ChargeRealMapper chargeRealMapper) throws Exception {
        double balanceAmount=0.0;
        if (chargePayRequestEntity.getCouponId()!=null&&!chargePayRequestEntity.getCouponId().equals("-1")){
         if (chargeRealMapper.couponIsTrue(chargePayRequestEntity.getCouponId(),chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode())==null){
             throw new Exception("无效的优惠券ID");
         }else {
             balanceAmount=chargeRealMapper.couponbalanceAmount(chargePayRequestEntity.getCouponId(),chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode());
         }
        }

        Charge charge=chargeRealMapper.findChargeProperty(chargePayRequestEntity.getChargeCode(),chargePayRequestEntity.getDatedif());
        ChargeDetail chargeDetail=chargeRealMapper.findChargeDetailProperty(chargePayRequestEntity.getChargeCode(),chargePayRequestEntity.getDatedif());
        RoomPerproty roomPerproty=chargeRealMapper.findRoomperproty(chargePayRequestEntity.getRoomCode());
        double roomSize=getRoomSize(chargeDetail.getAreaMin(),chargeDetail.getAreaMax(),roomPerproty.getRoomSize());
        double roomMoney=roomSize*(double)chargeDetail.getDatedif()*charge.getChargeStandard();
        if (roomMoney<=balanceAmount){
            roomMoney=0.0;
        }else {
            roomMoney-=balanceAmount;
        }
        roomMoney*=charge.getDiscount()*0.1;
        Double surplus=chargeRealMapper.getSurplus(chargePayRequestEntity.getRoomCode(),chargePayRequestEntity.getChargeCode());
        roomMoney=roomMoney-(surplus==null?0.0:surplus);
        return roomMoney>=0.0?roomMoney:0.0;
    }

    public double getRoomSize(double min,double max,double room) {
        if (min == 0 && max == 0) {
            return room;
        } else if (min != 0 && max == 0) {
            if (room < min) {
                return min;
            } else {
                return room;
            }
        } else if (min == 0 && max != 0) {
            if (max < room) {
                return max;
            } else {
                return room;
            }
        } else {
            if (room > min && room < max) {
                return room;
            } else if (room>max){
                return max;
            }else {
                return min;
            }
        }
    }

}
