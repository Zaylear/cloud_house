package com.rbi.admin.util;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * 生成UUID码
     * @return
     */
    public static String uuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    /**
     * 正则表达式过滤中文
     * @param str
     * @return
     */
    public static String filteringChinese(String str){
        String reg = "[\u2E80-\u9FFF]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(str);
        String repickStr = mat.replaceAll("");
        return repickStr;
    }

    /**
     * 使用正则表达式来判断字符串中是否包含字母
     * @param str 待检验的字符串
     * @return 返回Boolean值
     * true: 包含字母 ;false 不包含字母
     */
    public static boolean judgeContainsStr(String regex,String str) {
        Matcher m= Pattern.compile(regex).matcher(str);
        return m.matches();
    }

    /**
     * 判断对象中属性值是否全为空
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);

                System.out.print(f.getName() + ":");
                System.out.println(f.get(object));

                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static String randLetter(int count){
        String str = "";
        for (int i = 0;i<count;i++){
            str = str+ (char)(Math.random()*26+'A');
        }
        return str;
    }

    /**
     * 生成整形随机数
     * @param min
     * @param max
     * @return
     */
    public static String random(int min,int max){
        return String.valueOf(min+(int) (Math.random() * (max-min)));
    }

    public static void main(String[] args) {
        System.out.println(random(38,40));
    }

    public static String analysisStr(String str,String mode,int code)
    {
        int y=code-1;
        String target=null;
        String[] s=str.split(mode);
        for (int i = 0; i < s.length; i++) {
            if(i==y)
            {
                target = s[i];
                break;
            }
        }
        return target;
    }

    /**
     * 字符串中包含指定字符数量
     * @param originalStr
     * @param specifiedCharacters
     * @return
     */
    public static int containCharactersNumber(String originalStr,String specifiedCharacters){
        String[] ch = originalStr.split("");

        int t = 0;
        for (int i = 0; i < ch.length; i++) {

            int mlen = i + specifiedCharacters.length();

            if (mlen > ch.length) {
                break;
            }
            String s = originalStr.substring(i, mlen);
            if (s.equals(specifiedCharacters)) {
                t++;
            }
        }
        return t;
    }

}
