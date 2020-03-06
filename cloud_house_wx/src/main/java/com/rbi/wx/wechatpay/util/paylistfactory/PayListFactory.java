package com.rbi.wx.wechatpay.util.paylistfactory;

public class PayListFactory {
   public static GetPayList getPayList(Integer type) throws Exception {
       GetPayList getPayList=null;
       switch (type){
           case 1:
               getPayList= new PropertyGetPayList();
               break;
           default:throw new Exception("未定义的匹配类型");

       }
       return getPayList;
   }
}
