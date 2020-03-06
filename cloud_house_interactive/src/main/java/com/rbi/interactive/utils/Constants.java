package com.rbi.interactive.utils;

import java.util.Calendar;

public class Constants {

    public final static Integer VILLAGE_ENABLE = 1;
    public final static String VILLAGE_CODE_SET = "ERROR";

    /**
     * 请求类型
     */
    public final static String APPLICATION_JSON = "application/json;charset=UTF-8";
    public final static String APPLICATION_X_WWW_FORM_URLLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * 请求主机
     */
//    120.78.156.30
//    139.9.225.140
    //工程主机
    public final static String ADMIN_HOST = "http://139.9.225.140:8847";
    public final static String AUTHENTICATION_HOST_TEST = "http://120.78.156.30:8848";//test
    public final static String AUTHENTICATION_HOST_PRO = "http://139.9.225.140:8848";//pro
    private final static String AUTHENTICATION_HOST = AUTHENTICATION_HOST_PRO;

    //导出文件存储IP
    public final static String FILE_HOST_TEST = "http://120.78.156.30:80";//test
    public final static String FILE_HOST_PRO = "http://139.9.225.140:80";//pro 需要到服务器新建文件夹 bills、reports
    private final static String FILE_HOST = FILE_HOST_PRO;


    /**
     * 请求URL
     */
    //登录
    public final static String LOGIN = AUTHENTICATION_HOST + "/cloud_house_authentication/login";
    //退出
    public final static String LOGOUT = AUTHENTICATION_HOST + "/cloud_house_authentication/logout";
    //刷新token
    public final static String REFRESH_TOKEN = AUTHENTICATION_HOST + "/cloud_house_authentication/refresh/appkey";
    //权限认证
    public static final String AUTH_PATH = AUTHENTICATION_HOST + "/cloud_house_authentication/permit/auth";
    public static final String TOKEN_VERIFICATION = AUTHENTICATION_HOST + "/cloud_house_authentication/token/verification";
//    //根据房间编号查询收费项目
//    public final static String FIND_CHARGE_ITEM = ADMIN_HOST + "/cloud_house_admin/charge/findByChargeCodes";
//    //根据收费项目编号和月数查询规则
//    public final static String FIND_RULTBYCHARGECODEANDDATEDIF = ADMIN_HOST + "/cloud_house_admin/charge/property";
//    //根据用户userIds查询用户信息
//    public final static String FINDBYUSERIDS = ADMIN_HOST + "/cloud_house_admin/customer/findByUserIds";
//    //根据房间代码roomCodes查询房间信息
//    public final static String FINDBYROOMCODES = ADMIN_HOST + "/cloud_house_admin/room/findByRoomCodes";
//    //根据userId查询员工信息
//    public final static String FINDBYUSERID_STAFF = ADMIN_HOST + "/cloud_house_admin/user/findByUserId";

    /**
     * uuid = 26875117-76f1-431f-a006-47ac0fa3a213
     * key = rbi2019
     */

    /**
     * token秘钥，请勿泄露，请勿随便修改
     */
    public static final String SECRET = "f62e4cf9293003ff14f1d373f4c1c40f";
    /**
     * token 过期时间: 12小时
     */
    public static final int CALENDARFIELD = Calendar.HOUR;
    public static final int CALENDARINTERVAL = 10;

    /**
     * URL正则匹配
     */
    public static final String LOGOUTREGEX = ".*/logout";
    public static final String AUTHENTICATION_REGEX = ".*/auth/.*";

    /**
     * 登陆模块代码
     */
    public static final String WX_MODULE = "CLOUD_HOUSE_WX";
    public static final String WEB_MODULE = "CLOUD_HOUSE_WEB";
    public static final String ADMIN_MODULE = "CLOUD_HOUSE_RBI";
    public static final String BD_MODULE = "CLOUD_HOUSE_DB";

    /**
     * RSA秘钥对
     */
    public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGWiDSBbGZ8TPs0aHHydVrSfPpTfZdTNzTbmhfHYLBDnmaEFzty/9Ezp+YDH51j/UP/qe3Fld6AS7bjFqPzLp0gT3nj24+MmRue//FDI1DgcL/tqM8fFuQ3cfAQs3swTBUkX9LwooMTAj9whi7PUQoMxcDMFblC9sdgNY/LWI7CwIDAQAB";
    public static final String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwgg" +
            "JcAgEAAoGBAMZaINIFsZnxM+zRocfJ1WtJ8+lN9l1M3NNuaF8dgsEOeZoQXO3L/0TOn5gMfnWP9Q/+" +
            "p7cWV3oBLtuMWo/MunSBPeePbj4yZG57/8UMjUOBwv+2ozx8W5Ddx8BCzezBMFSRf0vCigxMCP3CGL" +
            "s9RCgzFwMwVuUL2x2A1j8tYjsLAgMBAAECgYAyIc1xx6afterdzA4LpOOf/kz7hozh/+9AMQOIJrlJ" +
            "ehMZ8VDLeWfHSEHl+CJuLDhQFpjlGWx6lZn2O38YITJulafbjwwJcBLBk94i5cBxTQEewbMujKJZft" +
            "s77dD0Ahcfg2JOcyBGYg3QhQYCoavH1PiTnUpodkeOlGI1t7xTYQJBAPAzwBalbfa78X/Z1+OX5Z1z" +
            "I0b5cYO9VnBba3axyxSicNJLeX2gPhLHmtGW/gpPm/maam9iknswtBpKOMvjj88CQQDTZcD9ZXW5xQ" +
            "Yf8mMN1E6pW/tNke0ZXoUWnY8zpWhJxyw5VQ14rVJMeoftIR5TEJdXN1ksTk55a7ZOfImI19QFAkEA" +
            "49Mxzkj6E5wPYwRsECJyVvRt8VOGpz1eTxNmyh24UMoB2HRdtxnVxlosLb7ZFU6M0iHz8dG8vbtkF8" +
            "tuM3STYQJAZR0PkWSdSc0On+s9K9k4AtPnQPQil3bZa7bTTx2340osQgQHnIgyjDgzmd20doDCEsp7" +
            "kbCm4nb9zU7OLjElzQJAD2fB4xjxzyaW8xn8obomljbruaF8fFLMeefVwbLu3Lbj1wGDLR1PBmWepa" +
            "0zINyjupZaoExPpld2YfijQb+VbA==";


    /**
     * 保存文件路径
     * 字体设置
     * * Ubuntu 默认字体
     * * DejaVuSans-Bold.ttf
     * * DejaVuSans.ttf
     * * DejaVuSansMono-Bold.ttf
     * * DejaVuSansMono.ttf
     * * DejaVuSerif-Bold.ttf
     * * DejaVuSerif.ttf
     */
    public final static String WIN_SAVE_BILLS_PATH = "D:/";
    public final static String UBUNTU_SAVE_BILLS_PATH_TEST = "/var/www/html/cloud_house/bills/";
    public final static String UBUNTU_SAVE_BILLS_PATH_PRO = "/usr/work/tomcat/80-apache-tomcat-8.5.35/webapps/cloud_house/bills/";
    public final static String SAVE_BILLS_PATH = UBUNTU_SAVE_BILLS_PATH_PRO;
    public final static String READ_BILLS_PATH = FILE_HOST+"/cloud_house/bills/";

    public final static String UBUNTU_FONT_PATH = "/usr/share/fonts/truetype/dejavu/simfang.ttf";
    public final static String WINDONWS_FONT_PATH = "C:/Windows/Fonts/simfang.ttf";
    //    public final static String WINDONWS_FONT_PATH = "C:/Windows/Fonts/SIMYOU.TTF";
    public final static String FONT_PATH = UBUNTU_FONT_PATH;

    public final static String REPORT_SAVE_PATH_TEST = "/var/www/html/cloud_house/reports/";
    public final static String REPORT_SAVE_PATH_PRO = "/usr/work/tomcat/80-apache-tomcat-8.5.35/webapps/cloud_house/reports/";
    public final static String UBUNTU_REPORT_FILE_PATH = FILE_HOST+"/cloud_house/reports/";
    public final static String REPORT_FILE_PATH = REPORT_SAVE_PATH_PRO;

    /**
     * Redis Key存储方式
     */
    public final static String REDISKEY_PROJECT = "cloud_house:";
    public final static String VILLAGE_TREE = "village_tree:";
    public final static String PROPERTY_DUE_TIME = "property_due_time:";
}
