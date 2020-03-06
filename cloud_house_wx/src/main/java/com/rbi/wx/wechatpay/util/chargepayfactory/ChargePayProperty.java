package com.rbi.wx.wechatpay.util.chargepayfactory;

import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.paymentrequestentity.ChargePayRequestEntity;
import com.rbi.wx.wechatpay.util.DateUtil;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * 物业的工厂对象
 */
public class ChargePayProperty implements ChargePayFactoryInterface{
    @Override
    public ChargePayDTO getChargePayDTO (ChargePayRequestEntity chargePayRequestEntity, PayMentMapper payMentMapper) {
        try {
            String time= DateUtil.getAfterDay(chargePayRequestEntity.getStartTime(),Integer.valueOf(chargePayRequestEntity.getDatedif()));
            chargePayRequestEntity.setEndTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Double returnMoney=payMentMapper.selectReturnMoney(chargePayRequestEntity.getRoomCode());  //退还费用;
        returnMoney=returnMoney==null?0.0:returnMoney;
        Double threeWayMoney=payMentMapper.selectThreeWayMoney(chargePayRequestEntity.getRoomCode());//三通费
        threeWayMoney=threeWayMoney==null?0.0:threeWayMoney;
        Double standard=payMentMapper.getMoney(chargePayRequestEntity.getChargeCode());
        standard=standard==null?0.0:standard;
        ChargePayDTO chargePayDTO=new ChargePayDTO();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        chargePayDTO.setDatedif(chargePayRequestEntity.getDatedif());
        chargePayDTO.setRoomCode(chargePayRequestEntity.getRoomCode());
        Double surplus=payMentMapper.getSurplus(chargePayDTO.getRoomCode(),chargePayRequestEntity.getOpenId());
        surplus=surplus==null?0.0:surplus;
        Double roomSize=payMentMapper.getRoomSize(chargePayRequestEntity.getRoomCode());
        //

        roomSize=this.getRoomSize(payMentMapper.getAreaMax(chargePayRequestEntity.getChargeCode(),chargePayRequestEntity.getDatedif()),payMentMapper.getAreaMin(chargePayRequestEntity.getChargeCode(),chargePayRequestEntity.getDatedif()),roomSize);
        double baseMoney=Double.valueOf(decimalFormat.format(roomSize*standard));

        //

        standard=Double.valueOf(decimalFormat.format(roomSize*standard));
        standard=Double.valueOf(decimalFormat.format(standard));    //求出应该缴费的金额
        standard*=Integer.valueOf(chargePayRequestEntity.getDatedif());
        chargePayDTO.setNewMoney(standard+"");
        if (chargePayRequestEntity.getCouponId()==null||chargePayRequestEntity.getCouponId().equals("-1")){
            chargePayDTO.setNewMoney(standard+"");
            chargePayDTO.setCouponMoney("null");
        }else {
            double couponMoney=payMentMapper.getCouponMoney(chargePayRequestEntity.getCouponId());
            if (couponMoney>=Double.valueOf(chargePayDTO.getNewMoney())) {
                couponMoney = Double.valueOf(decimalFormat.format(couponMoney));
                chargePayDTO.setCouponMoney(standard + "");
                chargePayDTO.setNewMoney(0 + "");
            }else {
                couponMoney = Double.valueOf(decimalFormat.format(couponMoney));
                chargePayDTO.setCouponMoney(couponMoney + "");
                chargePayDTO.setNewMoney((standard - couponMoney) + "");
            }
        }
        //下面是三通费抵扣
        if (threeWayMoney<=0.0){
            chargePayDTO.setThreeWayFee(threeWayMoney+"");
        }else if (threeWayMoney>=0.0&&Double.valueOf(chargePayDTO.getNewMoney())>0.0){
            if (threeWayMoney>=Double.valueOf(chargePayDTO.getNewMoney())){
                chargePayDTO.setThreeWayFee(chargePayDTO.getNewMoney());
                chargePayDTO.setNewMoney("0.0");
                chargePayDTO.setReturnMoney("0.0");
                chargePayDTO.setMoney("0.0");
            }else {
                chargePayDTO.setThreeWayFee(threeWayMoney+"");
                chargePayDTO.setNewMoney(Double.valueOf(chargePayDTO.getNewMoney())-threeWayMoney+"");
            }
        }
        //下面是退款项目抵扣
        if (returnMoney<=0.0&&Double.valueOf(chargePayDTO.getNewMoney())>0.0){
            chargePayDTO.setReturnMoney(returnMoney+"");
        }else {
            if (returnMoney>=Double.valueOf(chargePayDTO.getNewMoney())){
                chargePayDTO.setReturnMoney(chargePayDTO.getNewMoney());
                chargePayDTO.setNewMoney("0.0");
                chargePayDTO.setMoney("0.0");
            }else {
                chargePayDTO.setReturnMoney(returnMoney+"");
                chargePayDTO.setNewMoney(Double.valueOf(chargePayDTO.getNewMoney())-returnMoney+"");
            }
        }
        //下面是余额抵扣
        if (surplus<=0.0&&Double.valueOf(chargePayDTO.getNewMoney())>0.0){
            chargePayDTO.setMoney(surplus+"");
        }else {
            if (surplus>=Double.valueOf(chargePayDTO.getNewMoney())){
                chargePayDTO.setMoney(chargePayDTO.getNewMoney());
                chargePayDTO.setNewMoney("0.0");

            }else {
                chargePayDTO.setMoney(surplus+"");
                chargePayDTO.setNewMoney(Double.valueOf(chargePayDTO.getNewMoney())-returnMoney+"");
            }
        }

        double discount=payMentMapper.getChargeDiscount(chargePayRequestEntity.getDatedif(),chargePayRequestEntity.getChargeCode());
        double newMoney=Double.valueOf(chargePayDTO.getNewMoney())*discount*0.1;
        newMoney= Double.valueOf(decimalFormat.format(newMoney));
        chargePayDTO.setNewMoney(decimalFormat.format(newMoney)+"");
        chargePayDTO.setOldMoney(decimalFormat.format(standard)+"");
        chargePayDTO.setEndTime(chargePayRequestEntity.getEndTime());
        chargePayDTO.setStartTime(chargePayRequestEntity.getStartTime());
        return chargePayDTO;
    }
    public double getRoomSize(String min1,String max1,double room) {
        double min=0.0;
        double max=0.0;
        if (min1==null||min1.equals("")){

        }else {
            min=Double.valueOf(min1);
        }
        if (max1==null||max1.equals("")){

        }else {
            max=Double.valueOf(max1);
        }
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
