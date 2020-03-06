package com.rbi.admin.util;

import java.util.Calendar;

public class Constants {

    /**
     * 请求类型
     */
    public final static String APPLICATION_JSON = "application/json;charset=UTF-8";

    public final static String APPLICATION_X_WWW_FORM_URLLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * uuid = 26875117-76f1-431f-a006-47ac0fa3a213
     * key = rbi2019
     */

    /** token秘钥，请勿泄露，请勿随便修改 */
    public static final String SECRET = "f62e4cf9293003ff14f1d373f4c1c40f";
    /** token 过期时间: 12小时 */
    public static final int CALENDARFIELD = Calendar.HOUR;
    public static final int CALENDARINTERVAL = 10;

    /** URL正则匹配 */
    public static final String LOGOUTREGEX = ".*/logout";
    public static final String AUTHENTICATION_REGEX = ".*/auth/.*";

    /** 登陆模块代码 */
    public static final String WX_MODULE = "CLOUD_HOUSE_WX";
    public static final String WEB_MODULE = "CLOUD_HOUSE_WEB";
    public static final String ADMIN_MODULE = "CLOUD_HOUSE_RBI";
    public static final String BD_MODULE = "CLOUD_HOUSE_DB";

    public static final String AUTH_PATH = "/cloud_house_authentication/permit/auth";

    public static final String AUTH_HOST = "";

    public static final String AUTH_LOCAL_HOST = "http://120.78.156.30:8848";

    public final static String AUTHENTICATION_HOST = "http://139.9.225.140:8848";

    public static final String TOKEN_VERIFICATION = AUTH_LOCAL_HOST + "/cloud_house_authentication/token/verification";

    /**
     * Redis Key存储方式
     */
    public final static String REDISKEY_PROJECT = "cloud_house:";
    public final static String VILLAGE_TREE = "village_tree:";
    public final static String PROPERTY_DUE_TIME = "property_due_time:";

}
