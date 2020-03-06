package com.rbi.wx.wechatpay.util.successfactory;

import com.alibaba.fastjson.JSONArray;
import com.rbi.wx.wechatpay.dto.AllChargeDTO;
import com.rbi.wx.wechatpay.dto.paymentrecordentity.*;
import com.rbi.wx.wechatpay.mapper.PaySuccessMapper;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto.SuccessRoomDTO;

import java.util.List;
import java.util.Map;

public class EletricitySuccess implements SuccessInterface {
    @Override
    public void addOriginalBill(Map map, PaySuccessMapper paySuccessMapper, RSAGetStringUtil rsaGetStringUtil) {
        PayMentChargeEntity payMentChargeEntity=paySuccessMapper.getPayMentCharge(map.get("chargeCode").toString(),map.get("datedif").toString());
        PayMentPayerEntity payMentPayerEntity=paySuccessMapper.getPayMentPayerEntity(rsaGetStringUtil.getPrivatePassword(map.get("userId").toString()));
        PayMentRoomEntity payMentRoomEntity=paySuccessMapper.getPayMentRoomEntity(map.get("roomCode").toString());
        PayMentCouponEntity payMentCouponEntity=paySuccessMapper.getPayMentCouponEntity(map.get("couponId").toString());
        Double surplus=paySuccessMapper.getRoomSurplus(payMentRoomEntity.getRoomCode());
        Integer Datedif=1;
        //生成订单号
        String orderId = "";
        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
        Integer orderIdsCount = paySuccessMapper.findOrderIdsByorganizationId(payMentChargeEntity.getOrganizationId(),time);
        orderId = time+String.format("%08d", orderIdsCount);

        //
        //      应收
        double amountReceivable=payMentChargeEntity.getChargeStandard()
                *paySuccessMapper.getEletricitySum(map.get("roomCode").toString(),map.get("chargeCode").toString());
        //      实收
        double actualMoneyCollection=0.0;//amountReceivable*payMentChargeEntity.getDiscount()*0.1;
        //      优惠
        double preferentialAmount=amountReceivable-actualMoneyCollection;
        //      优惠券
        double couponMoney=0.0;
        if (payMentCouponEntity!=null){
            if (payMentCouponEntity.getBalanceAmount()>amountReceivable){
                couponMoney=amountReceivable;
                amountReceivable=0.0;
                paySuccessMapper.updateCoupon(couponMoney,payMentCouponEntity.getId().toString());
            }else {
                couponMoney=payMentCouponEntity.getBalanceAmount();
                amountReceivable-=couponMoney;
                paySuccessMapper.updateCoupon(couponMoney,payMentCouponEntity.getId().toString());
                paySuccessMapper.updateType(payMentCouponEntity.getId().toString());
            }}
        actualMoneyCollection=amountReceivable*payMentChargeEntity.getDiscount()*0.1;
        actualMoneyCollection-=surplus;
        updatrSurplus(payMentRoomEntity.getRoomCode(),paySuccessMapper);
        PayMentRecordEntity payMentRecordEntity=new PayMentRecordEntity();
        payMentRecordEntity.setOrderId(orderId);
        payMentRecordEntity.setActualMoneyCollection( actualMoneyCollection);
        payMentRecordEntity.setAmountReceivable(amountReceivable);
        payMentRecordEntity.setPreferentialAmount(preferentialAmount);
        /**
         * 拼装字符
         */
        AllChargeDTO allChargeDTO=new AllChargeDTO();
        allChargeDTO.setActualMoneyCollection(payMentRecordEntity.getActualMoneyCollection());
        allChargeDTO.setAmountReceivable(payMentRecordEntity.getAmountReceivable());
        allChargeDTO.setChargeCode(payMentChargeEntity.getChargeCode());
        allChargeDTO.setChargeName(payMentChargeEntity.getChargeName());
        allChargeDTO.setChargeStandard(payMentChargeEntity.getChargeStandard());
        allChargeDTO.setChargeType(payMentChargeEntity.getChargeType());
        allChargeDTO.setChargeUnit(payMentChargeEntity.getChargeUnit());
        allChargeDTO.setDatedif(Datedif);
        allChargeDTO.setPreferentialAmount(preferentialAmount);
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(allChargeDTO);
        paySuccessMapper.addPropertyBill(payMentRecordEntity,payMentChargeEntity,payMentRoomEntity,payMentPayerEntity,
                payMentPayerEntity.getUserPhone(),payMentCouponEntity,couponMoney,jsonArray.toJSONString());
        if (payMentCouponEntity!=null)
            payMentCouponEntity.setBalanceAmount(payMentCouponEntity.getBalanceAmount()/(double)Datedif);
        payMentRecordEntity.setPreferentialAmount(payMentRecordEntity.getPreferentialAmount()/(double)Datedif);
        payMentRecordEntity.setActualMoneyCollection(payMentRecordEntity.getActualMoneyCollection()/(double)Datedif);
        payMentRecordEntity.setAmountReceivable(payMentRecordEntity.getAmountReceivable()/(double)Datedif);
        for (int i=0;i<Datedif;i++){
            paySuccessMapper.addOriginalBill(payMentRecordEntity,payMentChargeEntity
                    ,payMentRoomEntity,payMentPayerEntity,payMentPayerEntity.getUserPhone(),payMentCouponEntity,couponMoney);
        }

    }
    private void updatrSurplus(String roomCode,PaySuccessMapper paySuccessMapper){
        Double surplus=paySuccessMapper.getRoomSurplus(roomCode);
        List<SuccessRoomDTO> list=paySuccessMapper.getSurplus(roomCode);
        for (SuccessRoomDTO successRoomDTO:list){
            if (successRoomDTO.getSurplus()>=surplus){
                paySuccessMapper.updateSurPlus(successRoomDTO.getId(),successRoomDTO.getSurplus()-surplus);
                return;
            }else {
                surplus-=successRoomDTO.getSurplus();
                paySuccessMapper.updateSurPlus(successRoomDTO.getId(),surplus-successRoomDTO.getSurplus());
            }
        }
    }
}
