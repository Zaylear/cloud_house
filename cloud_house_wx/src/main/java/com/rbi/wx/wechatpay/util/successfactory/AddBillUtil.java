package com.rbi.wx.wechatpay.util.successfactory;

import com.rbi.wx.wechatpay.mapper.PaySuccessMapper;
import com.rbi.wx.wechatpay.util.successfactory.sueccessfactorydto.SuccesssCouponDTO;
import com.rbi.wx.wechatpay.util.successmapperresult.*;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddBillUtil {
    private static  final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static void addBillDetailed(PaySuccessMapper paySuccessMapper
            , BillDetailedDTO billDetailedDTO
            , String actualMoneyCollection
            , String amountReceivable
            , String orderId
            , CustomerUserInfo customerUserInfo
            , RoomInfo roomInfo
            , String deductionRecord){
        Double amountDeductedThisTime=Double.valueOf(amountReceivable)-Double.valueOf(actualMoneyCollection);
        billDetailedDTO.setAmountDeductedThisTime(decimalFormat.format(amountDeductedThisTime));
        paySuccessMapper.insertBillDetailed(billDetailedDTO,actualMoneyCollection,amountReceivable,orderId,customerUserInfo,roomInfo,deductionRecord);
    }

    /**
     * 计算可抵扣金额的方法
     * @param oldDeductibleMoney       旧的可抵扣金额
     * @param deductibleMoney           抵扣的金额
     * @return
     */
    public static String getDeductibleMoney(String oldDeductibleMoney,String deductibleMoney){
        //结果
        Double result=0.0;
        result=Double.valueOf(oldDeductibleMoney)-Double.valueOf(deductibleMoney);
        return decimalFormat.format(result);
    }

    /**
     *获取新的已抵扣金额
     * @param oldDeductibledMoney      旧的已抵扣金额
     * @param deductibledMoney          抵扣的金额
     * @return
     */
    public static String getDeductibledMoney(String oldDeductibledMoney,String deductibledMoney){
        Double result=0.0;
        result=Double.valueOf(oldDeductibledMoney)+Double.valueOf(deductibledMoney);
        return decimalFormat.format(result);
    }

    /**
     * 获取新的剩余可抵扣余额
     * @param oldSurplusDeductibleMoney     旧的剩余可抵扣余额
     * @param SurplusDeductibleMoney        抵扣的金额
     * @return
     */
    public static String getSurplusDeductibleMoney(String oldSurplusDeductibleMoney,String SurplusDeductibleMoney){
        Double result=0.0;
        result=Double.valueOf(oldSurplusDeductibleMoney)+Double.valueOf(SurplusDeductibleMoney);
        return decimalFormat.format(result);
    }

    /**
     * 获取新的抵扣记录
     * @param deductionRecord       旧的抵扣记录
     * @param money                 抵扣的金额
     * @return
     */
    public static String getDeductionRecord(String deductionRecord,String money){
        if (deductionRecord==null||deductionRecord.equals("")||deductionRecord.equals("0")){
            return money;
        }else {
            return ","+money;
        }
    }


    /**
     *扣减金额
     * @param couponId      优惠券ID
     * @param money         抵扣的金额
     */
    public static List<CostDeduction> updateCoupon(PaySuccessMapper paySuccessMapper, String couponId, String money, String deductionMethod){
        List<CostDeduction> list=new ArrayList<>();
        SuccesssCouponDTO successsCouponDTO=paySuccessMapper.getCoupon(couponId);
        CostDeduction costDeduction=new CostDeduction();
        costDeduction.setAmountDeductedThisTime(money);
        successsCouponDTO.setAmountDeductedThisTime(money);
        //可抵扣金额
        costDeduction.setDeductibleMoney(getDeductibleMoney(successsCouponDTO.getDeductibleMoney(),money));
        //已抵扣金额
        costDeduction.setDeductibledMoney(getDeductibledMoney(successsCouponDTO.getDeductibledMoney(),money));
        costDeduction.setDeductionCode("COUPON");
        costDeduction.setDeductionMethod(deductionMethod);
        costDeduction.setDeductionOrderId(successsCouponDTO.getId());
        costDeduction.setDeductionItem("物业代金券");
        costDeduction.setSurplusDeductibleMoney(getSurplusDeductibleMoney(successsCouponDTO.getSurplusDeductibleMoney(),money));
        //下面是组装物业费抵扣
        String deductionRecord=getDeductionRecord(successsCouponDTO.getDeductionRecord(),money);
        paySuccessMapper.updateCouponMoney(couponId,money,deductionRecord);
        list.add(costDeduction);
        return list;
    }

    public static void addCostDetailed(PaySuccessMapper paySuccessMapper,List<CostDeduction> costList,String orderNum,String organizationId,String deductionRecord){
        if (costList.size()<1){
            return;
        }
        paySuccessMapper.addCostDeduction(costList, orderNum, organizationId, deductionRecord);
    }
    /**
     * 添加订单
     * @param paySuccessMapper
     * @param addBillDTO
     */
    public static void addBill(PaySuccessMapper paySuccessMapper, AddBillDTO addBillDTO, RoomInfo roomInfo, CustomerUserInfo customerUserInfo){
        paySuccessMapper.addBill(customerUserInfo, addBillDTO, roomInfo);
    }

    /**
     * 更新三通费
     * @param roomCode
     * @param money         扣减的金额
     */
    public static List<CostDeduction> updateThreeWay(PaySuccessMapper paySuccessMapper,String roomCode,String money){
        List<ThreeWayDTO> threeWayDTOList=paySuccessMapper.getThreeWayList(roomCode);
        List<CostDeduction> costDeductions=new ArrayList<>();
        Double surplusMoney=Double.valueOf(money);
        if (Double.valueOf(money)==0.0){
            return costDeductions;
        }
        if (threeWayDTOList==null||threeWayDTOList.size()<1){
            return costDeductions;
        }else {
            for (ThreeWayDTO threeWayDTO:threeWayDTOList){

                if (Double.valueOf(threeWayDTO.getSurplusDeductibleMoney())>=surplusMoney){
                    CostDeduction costDeduction=new CostDeduction();
                    costDeduction.setAmountDeductedThisTime(surplusMoney+"");
                    costDeduction.setDeductibleMoney(getDeductibleMoney(threeWayDTO.getDeductibleMoney(),money));
                    costDeduction.setDeductibledMoney(getDeductibledMoney(threeWayDTO.getDeductibledMoney(),money));
                    costDeduction.setDeductionCode("THREE_WAY_FEE");
                    costDeduction.setDeductionOrderId(threeWayDTO.getBillDetailedId());
                    costDeduction.setDeductionMethod("全额抵扣");
                    costDeduction.setSurplusDeductibleMoney(getSurplusDeductibleMoney(threeWayDTO.getSurplusDeductibleMoney(),money));
                    costDeduction.setChargeType(threeWayDTO.getChargeType());
                    costDeduction.setDiscount(threeWayDTO.getDiscount());
                    costDeduction.setDeductionItem(threeWayDTO.getChargeName());

                    String deductionRecord=getDeductionRecord(threeWayDTO.getDeductionRecord(),money);
                    paySuccessMapper.updateThreeWay(threeWayDTO.getBillDetailedId(),surplusMoney+"",deductionRecord);
                    costDeductions.add(costDeduction);
                    return costDeductions;
                }else {
                    CostDeduction costDeduction=new CostDeduction();
                    costDeduction.setAmountDeductedThisTime(threeWayDTO.getSurplusDeductibleMoney()+"");
                    costDeduction.setDeductibleMoney(getDeductibleMoney(threeWayDTO.getDeductibleMoney(),money));
                    costDeduction.setDeductibledMoney(getDeductibledMoney(threeWayDTO.getDeductibledMoney(),money));
                    costDeduction.setDeductionCode("THREE_WAY_FEE");
                    costDeduction.setDeductionOrderId(threeWayDTO.getBillDetailedId());
                    costDeduction.setDeductionMethod("部分抵扣");
                    costDeduction.setSurplusDeductibleMoney(getSurplusDeductibleMoney(threeWayDTO.getSurplusDeductibleMoney(),money));
                    costDeduction.setChargeType(threeWayDTO.getChargeType());
                    costDeduction.setDiscount(threeWayDTO.getDiscount());
                    costDeduction.setDeductionItem(threeWayDTO.getChargeName());
                    String deductionRecord=getDeductionRecord(threeWayDTO.getDeductionRecord(),money);
                    paySuccessMapper.updateThreeWay(threeWayDTO.getBillDetailedId(),threeWayDTO.getSurplusDeductibleMoney(),deductionRecord);
                    surplusMoney-=Double.valueOf(threeWayDTO.getSurplusDeductibleMoney());
                    costDeductions.add(costDeduction);
                }
            }
        }
        return costDeductions;
    }

    /**
     * 更新退还费用
     * @param paySuccessMapper
     * @param roomCode
     * @param money
     */
    public static List<CostDeduction> updateRefundMoney(PaySuccessMapper paySuccessMapper,String roomCode,String money){
        List<RefundMoneyDTO> refundMoneyDTOList=paySuccessMapper.getRefundMoney(roomCode);
        List<CostDeduction> refundCost=new ArrayList<>();
        Double surplusMoney=Double.valueOf(money);
        if (Double.valueOf(money)==0.0){
            return refundCost;
        }
        if (refundMoneyDTOList==null||refundMoneyDTOList.size()<1){
            return refundCost;
        }else {
            for (RefundMoneyDTO refundMoneyDTO:refundMoneyDTOList){
                if (Double.valueOf(refundMoneyDTO.getSurplusDeductibleMoney())>=surplusMoney){
                    CostDeduction costDeduction=new CostDeduction();
                    costDeduction.setAmountDeductedThisTime(surplusMoney+"");
                    costDeduction.setDeductibleMoney(getDeductibleMoney(refundMoneyDTO.getDeductibleMoney(),money));
                    costDeduction.setDeductibledMoney(getDeductibledMoney(refundMoneyDTO.getDeductibledMoney(),money));
                    costDeduction.setDeductionCode("REFUND");
                    costDeduction.setDeductionOrderId(refundMoneyDTO.getId());
                    costDeduction.setDeductionMethod("全额抵扣");
                    costDeduction.setSurplusDeductibleMoney(getSurplusDeductibleMoney(refundMoneyDTO.getSurplusDeductibleMoney(),money));
                    costDeduction.setDeductionItem("退款款项");
                    String deductionRecord=getDeductionRecord(refundMoneyDTO.getDeductionRecord(),money);
                    refundCost.add(costDeduction);
                    paySuccessMapper.updateRefundMoney(refundMoneyDTO.getId(),surplusMoney+"",deductionRecord);
                    return refundCost;
                }else {
                    CostDeduction costDeduction=new CostDeduction();
                    costDeduction.setAmountDeductedThisTime(refundMoneyDTO.getSurplusDeductibleMoney()+"");
                    costDeduction.setDeductibleMoney(getDeductibleMoney(refundMoneyDTO.getDeductibleMoney(),money));
                    costDeduction.setDeductibledMoney(getDeductibledMoney(refundMoneyDTO.getDeductibledMoney(),money));
                    costDeduction.setDeductionCode("REFUND");
                    costDeduction.setDeductionOrderId(refundMoneyDTO.getId());
                    costDeduction.setDeductionMethod("部分抵扣");
                    costDeduction.setSurplusDeductibleMoney(getSurplusDeductibleMoney(refundMoneyDTO.getSurplusDeductibleMoney(),money));
                    costDeduction.setDeductionItem("退款款项");
                    String deductionRecord=getDeductionRecord(refundMoneyDTO.getDeductionRecord(),money);
                    refundCost.add(costDeduction);
                    paySuccessMapper.updateRefundMoney(refundMoneyDTO.getId(),surplusMoney+"",deductionRecord);
                    surplusMoney-=Double.valueOf(refundMoneyDTO.getSurplusDeductibleMoney());
                }
            }
            return refundCost;
        }
    }

    /**
     * 更新账户余额
     * @param paySuccessMapper
     * @param roomCode
     * @param money
     */
    public static List<CostDeduction> updateSurplusMoney(PaySuccessMapper paySuccessMapper,String roomCode,String userCode,String money,String surplus){
        SurplusDTO surplusDTO=paySuccessMapper.getSurplusDTO(roomCode,userCode);
        List<CostDeduction> costDeductions=new ArrayList<>();
        if (surplusDTO==null){
            return costDeductions;
        }
        Double surplusMoney=Double.valueOf(money);
        if (Double.valueOf(money)==0.0){
            return costDeductions;
        }
        if (Double.valueOf(surplus)>=Double.valueOf(money)){
            CostDeduction costDeduction=new CostDeduction();
            costDeduction.setAmountDeductedThisTime(money);
            costDeduction.setDeductibleMoney(getDeductibleMoney(surplus,money));
            costDeduction.setDeductibledMoney("0.0");
            costDeduction.setDeductionCode("SURPLUS");
            costDeduction.setDeductionMethod("全额抵扣");
            costDeduction.setDeductionItem("账户余额");
            costDeductions.add(costDeduction);
            paySuccessMapper.updateSurplusDTO(surplusDTO.getId(),money);
        }else {
            CostDeduction costDeduction=new CostDeduction();
            costDeduction.setAmountDeductedThisTime(surplus);
            costDeduction.setDeductibleMoney("0.0");
            costDeduction.setDeductibledMoney("0.0");
            costDeduction.setDeductionCode("SURPLUS");
            costDeduction.setDeductionMethod("部分抵扣");
            costDeduction.setDeductionItem("账户余额");
            costDeductions.add(costDeduction);
            paySuccessMapper.updateSurplusDTO(surplusDTO.getId(),surplusDTO.getSurplus());
        }
      return costDeductions;
    }
}
