package com.rbi.wx.wechatpay.util.successfactory;


import com.rbi.wx.wechatpay.mapper.PaySuccessMapper;

import com.rbi.wx.wechatpay.util.DateUtil;

import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto.SuccessRoomDTO;
import com.rbi.wx.wechatpay.util.successmapperresult.*;

import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Transactional
public class PropertySuccess  implements SuccessInterface {
   // private ThisSystemOrderId thisSystemOrderId=SpringUtil.getBean(ThisSystemOrderId.class);

    @Override
    public void addOriginalBill (Map map, PaySuccessMapper paySuccessMapper, RSAGetStringUtil rsaGetStringUtil) {
        /**
         * 下面是重构的代码
         */

        String orderNum=map.get("orderId").toString();
        String dueTime=map.get("dueTime").toString();
        String startTime="";
        try {
          startTime= DateUtil.getAfterDay(dueTime,Integer.valueOf(map.get("datedif").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String deductionRecord="0";
        //抵扣信息集合
        List<CostDeduction> costDeductions=new ArrayList<>();
        //获取房间信息
        RoomInfo roomInfo=paySuccessMapper.getRoomInf(map.get("roomCode").toString());

        //获取缴费用户信息
        CustomerUserInfo customerUserInfo=paySuccessMapper.getCustomerUserInfo(rsaGetStringUtil.getPrivatePassword(map.get("userId").toString()));
        //获取缴费项目信息
        ChargeInfo chargeInfo=paySuccessMapper.getChargeInfo(map.get("chargeCode").toString(),map.get("datedif").toString());
        //实收总金额
        BillDetailedDTO billDetailedDTO=new BillDetailedDTO(chargeInfo);
        billDetailedDTO.setStartTime(dueTime);
        billDetailedDTO.setDueTime(startTime);
        billDetailedDTO.setDatedif(map.get("datedif").toString());
        billDetailedDTO.setChargeCode(map.get("chargeCode").toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double baseMoney=Double.valueOf(decimalFormat.format(Double.valueOf(roomInfo.getRoomSize())*Double.valueOf(chargeInfo.getChargeStandard())));
        //实收总金额
        double amountTotalReceivable=baseMoney*
                Double.valueOf(chargeInfo.getDiscount())*0.1*
                Double.valueOf(chargeInfo.getDatedif());
        //应收金额
        double amountReceivable=baseMoney*
                Double.valueOf(chargeInfo.getDiscount())*0.1*
                Double.valueOf(chargeInfo.getDatedif());
        //优惠券信息
        CouponDTO couponDTO;
        /**
         * 有优惠券的抵扣
         */

        String couponId=map.get("couponId").toString();

        if (!couponId.equals("-1")){
            couponDTO=paySuccessMapper.getCouponInfo(map.get("couponId").toString());
            if (Double.valueOf(couponDTO.getSurplusDeductibleMoney())>=amountTotalReceivable){
                //组装订单拆分实体
                deductionRecord=amountTotalReceivable+"";
                amountTotalReceivable=0.0;
                List<CostDeduction> couponList=AddBillUtil.updateCoupon(paySuccessMapper,couponDTO.getId(),amountTotalReceivable+"","部分抵扣");
                costDeductions.addAll(couponList);
                AddBillDTO addBillDTO=new AddBillDTO();
                addBillDTO.setOrderId(map.get("orderId").toString());
                addBillDTO.setActualTotalMoneyCollection(0.0+"");
                addBillDTO.setAmountTotalReceivable(amountReceivable+"");
                AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
                AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                        map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
                return;
                /**
                 * 计算订单  扣减余额的代码
                 */
            }else {
                //更新实收
                amountTotalReceivable=amountTotalReceivable-Double.valueOf(couponDTO.getSurplusDeductibleMoney());
                List<CostDeduction> couponCost=AddBillUtil.updateCoupon(paySuccessMapper,couponDTO.getId(),couponDTO.getSurplusDeductibleMoney()+"","全额抵扣");
                deductionRecord=amountTotalReceivable+"";
                costDeductions.addAll(couponCost);
             //   AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                /**
                 * 计算订单  扣减余额的代码
                 */
            }
        }

        /**
         * 三通费抵扣
         */

        //获取三通费
            Double threeWayMoney=paySuccessMapper.selectThreeWayMoney(map.get("roomCode").toString());//三通费
            threeWayMoney=threeWayMoney==null?0.0:threeWayMoney;
            BillDetailedDTO threeBillDetailedDTO=new BillDetailedDTO(chargeInfo);
            if (threeWayMoney!=0.0) {
                if (threeWayMoney >= amountTotalReceivable) {
                    //组装订单拆分实体
                    if (deductionRecord.equals("0"))
                    {
                        deductionRecord=(amountTotalReceivable+"");
                    }else {
                        deductionRecord+=","+amountTotalReceivable;
                    }
                    amountTotalReceivable=0.0;
                    List<CostDeduction> threeWayCost=AddBillUtil.updateThreeWay(paySuccessMapper,map.get("roomCode").toString(),amountTotalReceivable+"");
                    AddBillDTO addBillDTO=new AddBillDTO();
                    addBillDTO.setOrderId(map.get("orderId").toString());
                    addBillDTO.setActualTotalMoneyCollection(0.0+"");
                    addBillDTO.setAmountTotalReceivable(amountReceivable+"");
                    AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
                    costDeductions.addAll(threeWayCost);
                    AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                            map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
                    AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                    return;
                    /**
                     * 计算订单  扣减余额的代码
                     */
                } else {
                    if (deductionRecord.equals("0"))
                    {
                        deductionRecord=(threeWayMoney+"");
                    }else {
                        deductionRecord+=","+threeWayMoney;
                    }
                    amountTotalReceivable = amountTotalReceivable - threeWayMoney;
                    List<CostDeduction> threeWayCost= AddBillUtil.updateThreeWay(paySuccessMapper,map.get("roomCode").toString(),threeWayMoney+"");
                    costDeductions.addAll(threeWayCost);
              //      AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                    /**
                     * 计算订单  扣减余额的代码
                     */
                }
            }
        /**
         * 退款费用抵扣
         */
        Double returnMoney=paySuccessMapper.selectReturnMoney(map.get("roomCode").toString(),roomInfo.getOrganizationId());  //退还费用;
        returnMoney=returnMoney==null?0.0:returnMoney;
        if (returnMoney!=0.0) {
            BillDetailedDTO returnMoneyBillDetailedDTO = new BillDetailedDTO(chargeInfo);
            if (returnMoney >= amountTotalReceivable) {
                if (deductionRecord.equals("0"))
                {
                    deductionRecord=(amountTotalReceivable+"");
                }else {
                    deductionRecord+=","+amountTotalReceivable;
                }
                //组装订单拆分实体
                amountTotalReceivable=0.0;
                AddBillDTO addBillDTO=new AddBillDTO();
                addBillDTO.setOrderId(map.get("orderId").toString());
                addBillDTO.setActualTotalMoneyCollection(0.0+"");
                addBillDTO.setAmountTotalReceivable(amountReceivable+"");
                AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
                AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                        map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
                AddBillUtil.updateRefundMoney(paySuccessMapper,map.get("roomCode").toString(),amountTotalReceivable+"");
                AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                return;
                /**
                 * 计算订单  扣减余额的代码
                 */
            } else {
                if (deductionRecord.equals("0"))
                {
                    deductionRecord=(returnMoney+"");
                }else {
                    deductionRecord+=","+returnMoney;
                }
                amountTotalReceivable = amountTotalReceivable - returnMoney;
                AddBillUtil.updateRefundMoney(paySuccessMapper,map.get("roomCode").toString(),returnMoney+"");
            //    AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                /**
                 * 计算订单  扣减余额的代码
                 */
            }
        }
        /**
         * 账户余额抵扣
         */

        Double surplus=paySuccessMapper.getSurplusInfo(map.get("roomCode").toString(),rsaGetStringUtil.getPrivatePassword(map.get("userId").toString()));
        surplus=surplus==null?0.0:surplus;
        if (surplus!=0.0) {
            BillDetailedDTO surplusMoneyBillDetailedDTO = new BillDetailedDTO(chargeInfo);
            if (surplus >= amountTotalReceivable) {
                if (deductionRecord.equals("0"))
                {
                    deductionRecord=amountTotalReceivable+"";
                }else {
                    deductionRecord+=","+amountTotalReceivable;
                }
                //组装订单拆分实体
                AddBillDTO addBillDTO=new AddBillDTO();
                amountTotalReceivable=0.0;
                addBillDTO.setOrderId(map.get("orderId").toString());
                addBillDTO.setActualTotalMoneyCollection(amountTotalReceivable+"");
                addBillDTO.setAmountTotalReceivable(amountReceivable+"");
                AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
               List<CostDeduction> costDeductions1= AddBillUtil.updateSurplusMoney(paySuccessMapper,map.get("roomCode").toString(),rsaGetStringUtil.getPrivatePassword(map.get("userId").toString()),amountReceivable+"",surplus+"");
                AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                        map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
                costDeductions.addAll(costDeductions1);
                AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
                return;
                /**
                 * 计算订单  扣减余额的代码
                 */
            } else {
                if (deductionRecord.equals("0"))
                {
                    deductionRecord+=surplus;
                }else {
                    deductionRecord+=","+surplus;
                }
                amountReceivable = amountReceivable - surplus;
                AddBillDTO addBillDTO=new AddBillDTO();
                addBillDTO.setOrderId(map.get("orderId").toString());
                addBillDTO.setActualTotalMoneyCollection(amountTotalReceivable+"");
                addBillDTO.setAmountTotalReceivable(amountReceivable+"");
                AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
                AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                        map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
                List<CostDeduction> costDeductions1= AddBillUtil.updateSurplusMoney(paySuccessMapper,map.get("roomCode").toString(),rsaGetStringUtil.getPrivatePassword(map.get("userId").toString()),surplus+"",surplus+"");
                costDeductions.addAll(costDeductions1);
              //  AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);

                /**
                 * 计算订单  扣减余额的代码
                 */
            }
        }
        AddBillDTO addBillDTO=new AddBillDTO();
        addBillDTO.setOrderId(map.get("orderId").toString());
        addBillDTO.setActualTotalMoneyCollection(amountTotalReceivable+"");
        addBillDTO.setAmountTotalReceivable(amountReceivable+"");
        AddBillUtil.addBill(paySuccessMapper,addBillDTO,roomInfo,customerUserInfo);
        AddBillUtil.addBillDetailed(paySuccessMapper,billDetailedDTO,amountTotalReceivable+"",amountReceivable+"",
                map.get("orderId").toString(),customerUserInfo,roomInfo,deductionRecord);
        AddBillUtil.addCostDetailed(paySuccessMapper,costDeductions,orderNum,roomInfo.getOrganizationId(),deductionRecord);
    }

//    public static BillDetailedDTO assembleBillDetailed(ChargeInfo chargeInfo,BillDetailedDTO oldBill,BillDetailedDTO newBill){
//
//        if (oldBill.getAmountDeductedThisTime()==null||oldBill.getAmountDeductedThisTime().equals("")){
//            return newBill;
//        }else {
//            BillDetailedDTO billDetailedDTO=new BillDetailedDTO(chargeInfo);
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            Double amountDeductedThisTime=Double.valueOf(newBill.getAmountDeductedThisTime())+Double.valueOf(oldBill.getAmountDeductedThisTime());
//            billDetailedDTO.setAmountDeductedThisTime(decimalFormat.format(amountDeductedThisTime));
//        }
//    }

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
