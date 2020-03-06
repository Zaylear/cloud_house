package com.rbi.wx.wechatpay.util.receipt;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.NumberToCN;
import com.rbi.wx.wechatpay.util.receipt.entity.TableEntity;
import com.rbi.wx.wechatpay.util.receipt.entity.TableJson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;


public class PrinTable {
    private BaseFont baseFont;
    private Font tatalFont;
    private Font font;
    private Font font1;
    public PrinTable() throws IOException, DocumentException {
        baseFont = BaseFont.createFont("C:/Windows/Fonts/SIMYOU.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        font = new Font(baseFont);
        font.setSize(9F);
        font1 = new Font(baseFont);
        font1.setSize(7F);
        tatalFont=new Font(baseFont);
        font.setSize(12F);
    }

    /**
     * 判断o是否为空或者为0.0
     * @param o
     * @return
     */
    private String isNull(Object o){
       if(o instanceof String){
        if(o!=null){
            return o.toString();
        }
        return "";
        }
        else {
            if((double)o !=0.0){
                return o.toString();
            }
            return "";
        }
    }

    /**
     * 打印表格
     * @param tableEntity
     * @param document
     * @throws DocumentException
     */
    public void printTable(TableEntity tableEntity,Document document) throws DocumentException {
        document.add(new Paragraph("    "));
        document.add(new Paragraph("    "));
        document.add(new Paragraph("    "));
        document.add(new Paragraph("    "));
        printTatle(tableEntity,document,1);
       document.add(new Paragraph("    "));
       document.add(new Paragraph("    "));
        document.add(new Paragraph("  ---------------------------------------------------------------------------------------------------------------------------------------------------"));
        document.add(new Paragraph("    "));
        document.add(new Paragraph("    "));
        printTatle(tableEntity,document,0);
        printFoot(document);
    }

    /**
     * 打印表头
     * @param tableEntity
     * @param document
     * @param i
     * @throws DocumentException
     */
    private void printTatle(TableEntity tableEntity,Document document,int i) throws DocumentException {
        document.add(new Paragraph("                                   未来城客户缴费单("+ DateUtil.date("yyyy")+"年"+ DateUtil.date("MM")+"月"+")",tatalFont));
        font.setSize(9F);
        font.setStyle(Font.BOLD);
        document.add(new Paragraph("                                                                                                         抄表日期:"+tableEntity.getDoDate(),font));
        document.add(new Paragraph("       大楼名称:"+tableEntity.getHouseName()+"                                                                                     打印日期:"+tableEntity.getPrintDate(),font));
        document.add(new Paragraph("        ",font));
        printHeard(tableEntity,document);
        printTatle(document);
        ArrayList<TableJson> list=tableEntity.getTaleList();
        double mothSum=0.0;
        double lastSum=0.0;
        double doSum=0.0;
        for (TableJson t1:list){
            printPrice(t1,document);
                mothSum+=t1.getMonthPrice();
                lastSum+=t1.getLastMonthPrice();
                doSum+=t1.getSum();
        }
        printSum(mothSum,lastSum,doSum,document);
        printBigSum(doSum,document);
        printRemarks(tableEntity,document);
        printFoot(document,i);
    }

    /**
     * 打印列名
     * @param document
     * @throws DocumentException
     */
    private  void printTatle(Document document) throws DocumentException {
        PdfPTable table1 = new PdfPTable(9);
        table1.setTotalWidth(new float[] {96F,60F,35F,70F,45F,40F,90F,35F,35F});
        table1.addCell(getCell("         项目名称"));
        table1.addCell(getCell("建筑面积"));
        table1.addCell(getCell("标准单价"));
        table1.addCell(getCell("距上次缴费用量"));
        table1.addCell(getCell("缴费月数"));
        table1.addCell(getCell("折扣率"));
        table1.addCell(getCell("         计费期间"));
        table1.addCell(getCell("应交金额"));
        table1.addCell(getCell("实缴金额"));
        table1.setWidthPercentage(90);
        document.add(table1);
    }

    /**
     * 打印表格尾部
     * @param document
     * @param i
     * @throws DocumentException
     */
    private void printFoot(Document document,int i) throws DocumentException {
       PdfPTable headerTable =new PdfPTable(2);
       headerTable.setWidthPercentage(90);
       PdfPTable iTable =new PdfPTable(2);
       PdfPCell iCell;
       PdfPCell cell;
       iTable =new PdfPTable(1);
       iCell =getCell("     客户(签字):                                                               经办人(签字):");
       iCell.setVerticalAlignment(Element.ALIGN_CENTER);
       iCell.setFixedHeight(20F);
       iCell.setColspan(1);
       iTable.addCell(iCell);
       String msg="";
       if(i==1){
           msg="此联为物业留存";
       }else {
           msg="此份为业主留存";
       }
       iCell =getCell(msg);

       iCell.setFixedHeight(20F);
       iTable.addCell(iCell);
       cell =new PdfPCell(iTable);
       cell.setPadding(0);
       headerTable.addCell(cell);
       headerTable.setTotalWidth(new float[] {437F,71F});
       cell =getCell("盖章位置");
       cell.setPadding(0);
       headerTable.addCell(cell);
       document.add(headerTable);
   }

    /**
     * 打印备注的行
     * @param tableEntity
     * @param document
     * @throws DocumentException
     */
    private void printRemarks(TableEntity tableEntity,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table .addCell(getCell("备注: "+tableEntity.getRemark()));
        table .setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印金额大写的行
     * @param doSum
     * @param document
     * @throws DocumentException
     */
    private void printBigSum(double doSum,Document document) throws DocumentException {
        BigDecimal bigDecimal=new BigDecimal(doSum);
        String sum= NumberToCN.number2CNMontrayUnit(bigDecimal);
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[] {93F,410F});
        table.addCell(getCell("合计金额大写"));
        table.addCell(getCell(sum));
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印金额小写的行
     * @param mothSum
     * @param lastSum
     * @param doSum
     * @param document
     * @throws DocumentException
     */
    private void printSum(double mothSum,double lastSum,double doSum,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[] {398F,35F,35F,35F});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(getCell("合计金额小写:"));
        table.addCell(getCell(mothSum+""));
        table.addCell(getCell(lastSum+""));
        table.addCell(getCell(doSum+""));
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 获取输出内容
     * @param paragraph
     * @return
     */
    private PdfPCell getCell(String paragraph){
        PdfPCell cell=new PdfPCell();
        cell.setFixedHeight(15F);
        cell.setPhrase(new Paragraph(paragraph,font1));
        return cell;
    }

    /**
     * 打印表格中的数据
     * @param tableJson
     * @param document
     * @throws DocumentException
     */
    private void printPrice(TableJson tableJson, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(new float[] {96F,60F,35F,70F,45F,40F,90F,35F,35F});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell=getCell(isNull(tableJson.getName()));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell=getCell(isNull(tableJson.getHouseSize()));
        table.addCell(cell);
        table.addCell(getCell(isNull(tableJson.getLastMonth())));
        table.addCell(getCell(isNull(tableJson.getMonth())));
        table.addCell(getCell(isNull(tableJson.getMonthDosage())));
        table.addCell(getCell(isNull(tableJson.getPrice())));
        table.addCell(getCell(isNull(tableJson.getPriceDate())));
        table.addCell(getCell(isNull(tableJson.getMonthPrice())));
        table.addCell(getCell(isNull(tableJson.getLastMonthPrice())));
        table.addCell(getCell(isNull(tableJson.getSum())));
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印表格头部的数据
     * @param tableEntity
     * @param document
     * @throws DocumentException
     */
    private void printHeard(TableEntity tableEntity,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
          font1.setSize(7F);
       table.setTotalWidth(new float[] {60F,101.5F,74F,295.5f});
           table.addCell(new Paragraph("房间代码:",font1));
       table.addCell(new Paragraph(tableEntity.getHouseID(),font1));
       table.addCell(new Paragraph("客户名称:",font1));
         table.addCell(new Paragraph(tableEntity.getUsrName(),font1));
          table.setWidthPercentage(90);
          document.add(table);
    }

    /**
     * 把两张表分割开
     * @param document
     * @throws DocumentException
     */
    private void printFoot(Document document) throws DocumentException {
        document.add(new Paragraph("   ",tatalFont));
        document.add(new Paragraph("   ",tatalFont));
        document.add(new Paragraph("   ",tatalFont));
        document.add(new Paragraph("                                                          贵州菲斯赛维物业管理有限公司",tatalFont));
        document.add(new Paragraph("                                                                "+DateUtil.date("yyyy")+"年"+DateUtil.date("MM")+"月"+DateUtil.date("dd")+"日",tatalFont));
    }

}
