package com.rbi.wx.wechatpay.service.pdfservice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.rbi.wx.wechatpay.requestentity.JsonUtil;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.receipt.PrinTable;
import com.rbi.wx.wechatpay.util.receipt.entity.TableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.itextpdf.text.PageSize.A4;

@Service
public class PDFPossService {


    @Autowired
    private RedisUtil redisUtil;
    public JsonUtil saveDate(TableEntity tableEntity){
        String str= UUID.randomUUID().toString().replace("-","");
        if (this.redisUtil.set(str,tableEntity,1800)) {
            return new JsonUtil("200","请访问"+str);
        }else {
            return new JsonUtil("10003","服务器繁忙");
        }
    }
    public void printTable1(TableEntity tableEntity, HttpServletResponse response){
            Document document=new Document();
            Rectangle pagesize=new Rectangle(A4);
            document.setPageSize(pagesize);
            document.setMargins(0.1F,0.1F,0.1F,0.1F);
            PdfWriter pdfWriter= null;
            try {
                pdfWriter = PdfWriter.getInstance(document,response.getOutputStream());
                pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
                document.open();
                PrinTable prinTable=new PrinTable();
                prinTable.printTable(tableEntity,document);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.close();
            try {
                OutputStream outputStream=response.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    /**
    *生成小票的PDF方法
     */
    public void getDocument(HttpServletResponse response){
        Document document=new Document();
        Rectangle pagesize=new Rectangle(130.0F,240.0F);
        document.setPageSize(pagesize);
        document.setMargins(20F,20F,0.1F,0.1F);

        try {
            PdfWriter pdfWriter=PdfWriter.getInstance(document,response.getOutputStream());
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
            document.open();
            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/SIMYOU.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
           document.add(new Paragraph("中文",font));
           document.add(new Paragraph("test",font));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }


     /**
     *生成单据的方法
      */
     public void printTable(TableEntity tableEntity,HttpServletResponse response){
         Document document=new Document();
         Rectangle pagesize=new Rectangle(A4);
         document.setPageSize(pagesize);
         document.setMargins(0.1F,0.1F,0.1F,0.1F);
         PdfWriter pdfWriter= null;
         try {
             pdfWriter = PdfWriter.getInstance(document,response.getOutputStream());
             pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
             document.open();
             PrinTable prinTable=new PrinTable();
             prinTable.printTable(tableEntity,document);
         } catch (DocumentException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }


         document.close();

     }

}
