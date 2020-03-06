package com.rbi.wx.wechatpay.util.successfactory;

public class SuccessFactory {
   public static SuccessInterface getSuccess(String type) throws Exception {
        switch (type){
            case "1" :return new PropertySuccess();
            case "7" :return new EletricitySuccess();
            default:throw new Exception("参数错误");
        }
    }
}
