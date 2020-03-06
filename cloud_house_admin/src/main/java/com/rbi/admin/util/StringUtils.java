package com.rbi.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 一次性判断多个或单个对象为空。
	 * 
	 * @param objects
	 * @author
	 * @return 只要有一个元素为Blank，则返回true
	 */
	public static boolean isBlank(Object... objects) {
		Boolean result = false;
		for (Object object : objects) {
			if (object == null || "".equals(object.toString().trim()) || "null".equals(object.toString().trim())
					|| "[null]".equals(object.toString().trim()) || "[]".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 一次性判断多个或单个对象不为空。
	 * 
	 * @param objects
	 * @author
	 * @return 只要有一个元素为Blank，则返回flase
	 */
	public static boolean isNotBlank(Object... objects) {
		return !isBlank(objects);
	}

	/**
	 * 将String字符串转为Integer 为空则置为0
	 * 
	 * @param s
	 * @return
	 */
	public static Integer stringParseInt(String s) {
		if (isNotBlank(s)) {
			return Integer.parseInt(s.split("\\.")[0]);
		}
		return new Integer(0);
	}

	/**
	 * 将string字符串值小于0 置为空（为满足指标特殊计算要求）
	 * 
	 * @param s
	 * @return
	 */
	public static Integer stringParseInteger(String s) {
		if (isBlank(s)) {
			return null;
		} else if (stringParseInt(s) >= 0) {
			return stringParseInt(s);
		}
		return null;
	}

	/**
	 * 计算指标 为空则不加（满足指标特殊计算）
	 * 
	 * @param integers
	 * @return
	 */
	public static Integer sumIntegers(Integer... integers) {
		Integer result = 0;
		for (Integer i : integers) {
			if (isNotBlank(i)) {
				result += i;
			}
		}
		return result;
	}

	/**
	 * 计算指标 为空则不加（满足指标特殊计算）
	 * 
	 * @param doubles
	 * @return
	 */
	public static Double sumDoubles(Double... doubles) {
		// DecimalFormat df = new DecimalFormat("#.00");
		Double result = new Double(0);
		for (Double i : doubles) {
			if (isNotBlank(i)) {
				result += i;
			}
		}
		return result;
	}

	/**
	 * 字符串转为Double 如果字符串为空，则返回null 否则返回2位小数的double
	 * 
	 * @param s
	 * @return
	 */
	public static Double stringParseDouble(String s) throws Exception {
		DecimalFormat df = new DecimalFormat("#.0000");
		if (isBlank(s)) {
			return null;
		}
		return Double.parseDouble(df.format(Double.parseDouble(s)));
	}

	public static Double stringParseDoubleFull(String s) throws Exception {
		if (isBlank(s)) {
			return null;
		}
		return Double.parseDouble(s);
	}

	public static Double stringParseDoubleZero(String s) {
		try {
			if (isBlank(s)) {
				return new Double(0);
			}
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
		}
		return new Double(0);
	}

	/**
	 * 多个Integer取最大值
	 * 
	 * @param integer
	 * @return
	 */
	public static Integer max(Integer... integer) {
		Integer result = 0;
		for (Integer i : integer) {
			if (isNotBlank(i)) {
				result = Math.max(i, result);
			}
		}
		return result;
	}

	/**
	 * 多个Double取最大值
	 * 
	 * @param doubles
	 * @return
	 */
	public static Double max(Double... doubles) {
		Double result = 0.00;
		for (Double i : doubles) {
			if (isNotBlank(i)) {
				result = Math.max(i, result);
			}
		}
		return result;
	}

	/**
	 * 多个Double取最小值
	 * 
	 * @param doubles
	 * @return
	 */
	public static Double min(Double... doubles) {
		Double result = null;
		for (Double i : doubles) {

			if (isNotBlank(i)) {
				if (isBlank(result)) {
					result = i;
				}
				result = Math.min(i, result);
			}
		}
		return result;
	}

	/**
	 * 判断所有字符串全部都不为空 有一个为空则返回false,否则返回true
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean isNotAllBlank(Object... objects) {
		Boolean result = true;
		for (Object object : objects) {
			if (isBlank(object)) {
				result = false;
				return result;
			}
		}
		return result;
	}

	/**
	 * 判断字符串是否全部为空 只要有一个不为空返回false 全部为空返回true
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean isAllBlank(Object... objects) {
		Boolean result = true;
		for (Object object : objects) {
			if (isNotBlank(object)) {
				result = false;
				return result;
			}
		}
		return result;
	}

	/**
	 * 字符串转日期
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static Date stringParseDate(String s, String dataFormat) throws Exception {
		if (isNotBlank(s)) {
			SimpleDateFormat format = new SimpleDateFormat(dataFormat);
			Date date = format.parse(s);
			return date;
		}
		return null;
	}

	/**
	 * 年份相减，如果结果小于0，则置为空 (用于指标计算)
	 * 
	 * @param startDate
	 *            年份（被减数）
	 * @param endDate
	 *            年份（减数）
	 * @return
	 */
	public static Integer minusDate(Date startDate, Date endDate) {
		if (!isNotAllBlank(startDate, endDate)) {
			return null;
		}
		Integer result = null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endDate);
		Integer startYear = c1.get(Calendar.YEAR);
		Integer endYear = c2.get(Calendar.YEAR);
		result = endYear - startYear;
		if (result < 0) {
			return null;
		}
		return result;
	}

	/**
	 * 月份相减，如果结果小于0，则置为空 (用于指标计算)
	 * 
	 * @param startDate
	 *            年份（被减数）
	 * @param endDate
	 *            年份（减数）
	 * @return
	 */
	public static Integer minusMonth(Date startDate, Date endDate) {
		Integer result = null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endDate);
		Integer startMonth = c1.get(Calendar.MONTH) + 1;
		Integer endMonth = c2.get(Calendar.MONTH) + 1;

		result = endMonth - startMonth + minusDate(startDate, endDate) * 12;
		if (result < 0) {
			return null;
		}
		return result;
	}

	/**
	 * 判断所有double是否属于【0，1】区间 是返回true，否则返回false
	 * 
	 * @param doubles
	 * @return
	 */
	public static boolean belong(Double... doubles) {

		for (Double i : doubles) {
			if (isNotBlank(i) && (i < 0 || i > 1)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 日期转化字符串
	 * 
	 * @param date
	 * @param dateformat
	 * @return
	 */
	public static String dateParseString(Date date, String dateformat) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateformat);
			if (isNotBlank(date)) {
				return simpleDateFormat.format(date);
			}
			return "";
		} catch (Exception e) {
			logger.error("日期转换字符串出错 ", e);

			return "";
		}
	}

	/**
	 * 日期往前倒推几个月份
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateMiunsMonth(Date date, int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, month);
		return rightNow.getTime();
	}

	/**
	 * 判断对象为空则
	 * 
	 * @param object
	 * @return
	 */
	public static Object stringParseNull(Object object) {
		if (isNotBlank(object)) {
			return object;
		}
		return "";
	}
	
	/**
	 * 判断字符串中是否包含数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containNumber(String str){
		if(isNotBlank(str)){
			char[] array = str.toCharArray();
			for(char c: array){
				if(Character.isDigit(c)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * 判断字符串中是否包含指定范围字符
     * 
     * @param str
     * @return
     */
    public static boolean containNumber(String str, char start, char end){
        if(isNotBlank(str)){
            char[] array = str.toCharArray();
            for(char c: array){
                if(Character.isDigit(c)){
                    byte startByte = (byte)start;
                    byte endByte = (byte)end;
                    if(c >= startByte && c <= endByte){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
