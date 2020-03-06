package com.rbi.wx.wechatpay.util;

import java.math.BigDecimal;
import java.util.Date;

public class Tools {

    /**
     * 四舍五入保留两位小数
     * @param money
     * @return
     */
    public static double moneyHalfAdjust(Double money){
        double menney = (new BigDecimal(String.valueOf(money)).
                setScale(2,BigDecimal.ROUND_HALF_UP)).doubleValue();
        return menney;
    }

    public static String randm(){
        return "wlc"+new Date().getTime();
    }
    public static String random(int min,int max){
        return String.valueOf(min+(int) (Math.random() * (max-min)));
    }
}
