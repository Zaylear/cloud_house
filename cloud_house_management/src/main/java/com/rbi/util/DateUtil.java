package com.rbi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 初始化时间格式
     */
    public final static String FORMAT_PATTERN="yyyy-MM-dd HH:mm:ss";
    public final static String HOUR_PATTERN="yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_PATTERN="yyyyMMdd";
    public final static String MONTH_PATTERN="yyyyMM";
    public final static String MONTH_DAY_PATTERN="MM.dd";
    public final static String HOUR_MINUTE_PATTERN="HH:mm";

    /**
     * 获取当前时间
     * @param dateType
     * @return
     */
    public static String  date(String dateType){
        SimpleDateFormat df = new SimpleDateFormat(dateType);
        return df.format(new Date());
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static Long timeStamp(){
        return new Date().getTime();
    }
}
