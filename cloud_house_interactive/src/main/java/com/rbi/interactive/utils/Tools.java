package com.rbi.interactive.utils;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import io.micrometer.core.instrument.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
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

    /**
     * 判断list中是否包含字符串str
     * @param list
     * @param str
     * @return
     */
    public static boolean ifInclude(List<String> list,String str){
        for (String s:list){
            if (s.equals(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 生成pdf文件
     * @param savePath
     * @param fontPath
     */
    public static void createPDF(String savePath,String fontPath){
        try {

//            path = 订单号；

            savePath = "D:\\test.pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(savePath);

            Document document=new Document();
            Rectangle pagesize=new Rectangle(130.0F,240.0F);
            document.setPageSize(pagesize);
            document.setMargins(0.1F,0.1F,0.1F,0.1F);


            PdfWriter pdfWriter=PdfWriter.getInstance(document,fileOutputStream);
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
            document.open();
//            包含字体如下：
//            DejaVuSans-Bold.ttf
//            DejaVuSans.ttf
//            DejaVuSansMono-Bold.ttf
//            DejaVuSansMono.ttf
//            DejaVuSerif-Bold.ttf
//            DejaVuSerif.ttf
            BaseFont baseFont = BaseFont.createFont(Constants.FONT_PATH,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            document.add(new Paragraph("中文",font));
            document.add(new Paragraph("test",font));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 字符串解析
     * @param str 需解析的字符串
     * @param mode 字符串分割方式 示例   "\r\n"
     * @param code 取分割的字符串中的第几个字符串
     * @return
     */
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
     * 四舍五入保留两位小数
     * @param money
     * @return
     */
    public static double moneyHalfAdjust(Double money){
        double menney = (new BigDecimal(String.valueOf(money)).
                setScale(2,BigDecimal.ROUND_HALF_UP)).doubleValue();
        return menney;
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
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

    /**
     * 生成整形随机数
     * @param min
     * @param max
     * @return
     */
    public static String random(int min,int max){
        return String.valueOf(min+(int) (Math.random() * (max-min)));
    }

    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    public static List<String> stringsAsList(String strList){
        String str = strList.replaceAll("\\[","").replaceAll("]","");
        return Arrays.asList(str.split(","));
    }

    /**
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     *
     * @param str
     * @param target
     * @return
     */
    private static int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */
    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }





/**
 　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
 　　* @param sourceDate
 　　* @param formatLength
 　　* @return 重组后的数据
 　　*/

    public static String frontCompWithZore(int sourceDate,int formatLength) {
        /*
         * 0 指前面补充零
         * formatLength 字符总长度为 formatLength
         * d 代表为正数。
         */
        String newString = String.format("%0"+formatLength+"d", sourceDate);
        return newString;
    }

//    public static String thisSystemOrderId(){
//        String orderId =
//    }


    public static void main(String[] args) {

        System.out.println(frontCompWithZore(8,3));

        //测试生成ID前自动补零
        String newString = String.format("%08d", 5);
        System.out.println("newString === "+newString);


        //测试相似度算法
        String a= "Steel";
        String b = "Steel";
        System.out.println("相似度："+getSimilarityRatio(a,b));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lable",2000);
        jsonObject.put("value",2000);
        System.out.println(jsonObject);



//        createPDF("","");


//        System.out.println(moneyHalfAdjust(15.49414));

        System.out.println("发的三脚架");

        System.out.println( (int)Math.floor(2.1));

        String star = "2016-05-23";
        String end = null;
        for (int i = 0; i < 2; i++) {
            try {
                end = DateUtil.getAfterMonth(star,3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            star = end;
            System.out.println(star);
        }

        String phone = "18685564953";

        System.out.println(Pattern.matches(REGEX_MOBILE,phone));

        System.out.println(phone.charAt(2));
    }

    public static String changeBlank(String value){
        if (value.equals("blank")){
            return null;
        }else {
            return value;
        }
    }

}
