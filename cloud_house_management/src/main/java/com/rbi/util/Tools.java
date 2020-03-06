package com.rbi.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
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
            fontPath = "C:/Windows/Fonts/SIMYOU.TTF";
            String ubuntPath = "/usr/share/fonts/truetype/dejavu";
//            包含字体如下：
//            DejaVuSans-Bold.ttf
//            DejaVuSans.ttf
//            DejaVuSansMono-Bold.ttf
//            DejaVuSansMono.ttf
//            DejaVuSerif-Bold.ttf
//            DejaVuSerif.ttf
            BaseFont baseFont = BaseFont.createFont(fontPath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
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

    public static String randLetter(int count){
        String str = "";
        for (int i = 0;i<count;i++){
            str = str+ (char)(Math.random()*26+'A');
        }
        return str;
    }

    public static void main(String[] args) {
        String salt = uuid();
        System.out.println(salt);

        String password = null;
        try {
            password = Md5Util.getMD5("123456",salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(password);
        String savePath = null;String fontPath = null;
        try {
            createPDF(savePath,fontPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
