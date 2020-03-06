package com.rbi.interactive.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public final static String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String YEAR = "yyyy";
    public final static String MONTH = "MM";

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

    /**
     * 获取指定时间N个月后的时间
     * @param inputDate
     * @param number
     * @return
     * @throws ParseException
     */
    public static String  getAfterMonth(String inputDate,int number) throws ParseException {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = sdf.parse(inputDate);//初始日期
        c.setTime(date);//设置日历时间
        c.add(Calendar.MONTH,number);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }

    /**
     * 获取某个时间N天后的时间
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
        c.add(Calendar.DAY_OF_MONTH,number);//在日历的月份上增加6个月
        String strDate = sdf.format(c.getTime());//的到你想要得6个月后的日期
        return strDate;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 比较两时间大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两日期之间的天数
     * @param first
     * @param second
     * @return
     * @throws ParseException
     */
    public static int  longOfTwoDate(String first,String second) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date firstdate = format.parse(first);
        Date seconddate = format.parse(second);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstdate);
        int cnt = 0;
        while(calendar.getTime().compareTo(seconddate)!=0){
            calendar.add(Calendar.DATE, 1);
            cnt++;
        }
        return cnt;
    }

    /**
     * 计算两时间相差的月数
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
//    public static int getMonthSpace(String date1, String date2)
//            throws ParseException {
//
//        int result = 0;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//
//        c1.setTime(sdf.parse(date1));
//        c2.setTime(sdf.parse(date2));
//
//        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
//
//        return result == 0 ? 1 : Math.abs(result);
//
//    }

    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(String d1, String d2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(d1));
        c2.setTime(sdf.parse(d2));
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }


    /**
     * 获取某天的时间,支持自定义时间格式
     * @param simpleDateFormat 时间格式,yyyy-MM-dd HH:mm:ss
     * @param index 为正表示当前时间加天数，为负表示当前时间减天数
     * @return String
     */
    public static String getTimeDay(String date,String simpleDateFormat,int index) throws ParseException {
        SimpleDateFormat sj = new SimpleDateFormat(simpleDateFormat);

        Date d = sj.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, index);
        return sj.format(calendar.getTime());
    }

    /**
     * 获取指定月份的天数
     * @param year
     * @param month
     * @return
     */
    public static Integer daysMonth(int year,int month){
        int date=0;//此处必须为0
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取当前月份的下月1号
     * @return
     */
    public static String nextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
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
//        System.out.println(calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 判断该日期是否是该月的最后一天
     *
     * @param date
     *            需要判断的日期
     * @return
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     71    * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     72    *
     73    * @param strDate
     74    * @return
     75    */
    public static Date strToDateLong(String strDate,String dateType) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateType);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取指定月份的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR,year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }
    /**
     * 获得该月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay=0;
        //2月的平年瑞年天数
        if(month==2) {
            if(year%4==0&&year%100!=0||year%400==0){
                lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH)+1;
            }else{
                lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH);
            }
        }else {
            lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }


    /**
     * 获取指定日期的年份
     * @param dateStr
     * @return
     */
    public static int dateOfYear(Date dateStr){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStr);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的月份
     * @param dateStr
     * @return
     */
    public static int dateOfMonth(Date dateStr){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStr);
        return calendar.get(Calendar.MONTH)+1;
    }


    public static void main(String[] args) {
        System.out.println(compareDate("2019-02-09","2019-02-08"));

        System.out.println(dateOfMonth(DateUtil.strToDateLong("2019-02-08",DateUtil.YEAR_MONTH_DAY)));

        System.out.println(getLastDayOfMonth(2020,4));

        System.out.println(isFirstDayOfMonth(strToDateLong("2019-03-31",YEAR_MONTH_DAY)));

//        System.out.println(nextMonthFirstDate());
//
//        System.out.println(DateUtil.compareDate("2019-12-01","2019-12-18"));
//
//        try {
//            System.out.println(getMonthDiff("2019-01-01","2019-11-30"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println("当前月份的天数为："+daysMonth(2019,11));
    }

//    public static void main(String[] args) {
//        try {
//            System.out.println(getAfterDay("2016-12-01",274));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println(DateUtil.stampToDate(String.valueOf(DateUtil.timeStamp()+100*24*60*60*1000)));
//
//
//        try {
//            System.out.println(getMonthDiff("2016-12-01","2017-09-01"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            System.out.println(longOfTwoDate("2016-12-01","2017-09-01"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

}
