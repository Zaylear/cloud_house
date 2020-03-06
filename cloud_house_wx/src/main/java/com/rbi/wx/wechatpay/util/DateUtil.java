package com.rbi.wx.wechatpay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public final static String FORMAT_PATTERN="yyyy-MM-dd HH:mm:ss";
    public final static String HOUR_PATTERN="yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_PATTERN="yyyyMMdd";
    public final static String MONTH_PATTERN="yyyyMM";
    public final static String MONTH_DAY_PATTERN="MM.dd";
    public final static String HOUR_MINUTE_PATTERN="HH:mm";
    public final static String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_PATTERN = "yyyyMMddHHmmss";

    public static String  date(String dateType){
        SimpleDateFormat df = new SimpleDateFormat(dateType);
        return df.format(new Date());
    }
    public static Long timeStamp(){
        return new Date().getTime();
    }


    /**
     * 判断该日期是否是该月的第一天
     *
     * @param date
     *            需要判断的日期
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 获取某个时间N个月后的时间
     * @param inputDate
     * @param number
     * @return
     * @throws ParseException
     */
    public static String  getAfterDay(String inputDate,int number) throws ParseException {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = sdf.parse(inputDate);//初始日期
        c.setTime(date);//设置日历时间
        c.add(Calendar.MONTH,number);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }


    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }
}
