package com.rbi.wx.wechatpay.util.paymentlistutil;

import com.rbi.wx.wechatpay.dto.paymentlist.PayMentListPropertyDTO;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.ChargeRealProperty;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.RoomPerproty;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class GetPayMentListJson {
    public List<PayMentListUtil> getJson(List<PayMentListPropertyDTO> listPropertyDTOS, RoomPerproty roomPerproty){
        List<PayMentListUtil> payMentListUtils= new LinkedList<>();
        ChargeRealProperty chargeRealProperty=new ChargeRealProperty();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PayMentListPropertyDTO payMentListPropertyDTO1=listPropertyDTOS.get(0);
        double size=chargeRealProperty.getRoomSize(payMentListPropertyDTO1.getAreaMin(),payMentListPropertyDTO1.getAreaMax(),roomPerproty.getRoomSize());
        double baseMoney=Double.valueOf(decimalFormat.format(size*payMentListPropertyDTO1.getChangeStandard()*payMentListPropertyDTO1.getDatedif()));
        for (PayMentListPropertyDTO payMentListPropertyDTO:listPropertyDTOS){
            double oldMoney=Double.valueOf(decimalFormat.format(baseMoney*payMentListPropertyDTO.getDatedif()));
            double newMoney=Double.valueOf(decimalFormat.format(oldMoney*payMentListPropertyDTO.getDiscount()*0.1));
            payMentListUtils.add(new PayMentListUtil(payMentListPropertyDTO.getDatedif(),oldMoney,newMoney,payMentListPropertyDTO.getDiscount()));
        }
        return payMentListUtils;
    }
}
