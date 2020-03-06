package com.rbi.interactive.utils;

import java.text.ParseException;

public class LateFee {

    public static void main(String[] args) throws ParseException {


        String initialDate = "2019-08-20";
        String startTime = initialDate;
        int count = 0;
        int compareDateCount;
        String endTime = startTime;
        //统计季度
        while (count != -1){

            System.out.printf("startTime"+startTime+"\t");
            compareDateCount = DateUtil.compareDate(startTime,DateUtil.date(DateUtil.YEAR_MONTH_DAY));
            startTime = DateUtil.getAfterMonth(startTime,3);
            System.out.println();
            System.out.println(compareDateCount);
            System.out.println();
            System.out.print("comp:"+compareDateCount+"\n");
            if (compareDateCount==0){
                endTime = startTime;
                count++;
                break;
            }else if (compareDateCount==1){
                break;
            }
            endTime = startTime;
            count++;
        }

        System.out.println(endTime);
        int dayCount = DateUtil.longOfTwoDate(initialDate,endTime);
        System.out.println("相隔天数："+dayCount);

        startTime = initialDate;

        for (int i = 0; i < count; i++) {
            startTime = DateUtil.getAfterMonth(startTime,3);
            dayCount = DateUtil.longOfTwoDate(startTime,endTime);
            System.out.printf("最后相隔天数差："+dayCount+"\n");
        }

    }

//    public static void main(String args[]) {
//        int i= compare_date("2016-01-01", "2019-08-16");
//        System.out.println("i=="+i);
//    }
//
//    public static int compare_date(String DATE1, String DATE2) {
//
//
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date dt1 = df.parse(DATE1);
//            Date dt2 = df.parse(DATE2);
//            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1 在dt2前");
//                return 1;
//            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
//                return -1;
//            } else {
//                return 0;
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return 0;
//    }

}
