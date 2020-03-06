package com.rbi.util;

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

    /** RSA秘钥对 */
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

}
