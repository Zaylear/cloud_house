package com.rbi.interactive.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import com.rbi.interactive.entity.dto.BillHeaderDTO;
import com.rbi.interactive.entity.dto.BillSubjectDTO;
import com.rbi.interactive.entity.CostDeductionDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;


public class PrintPDFTable {

    private final static Logger logger = LoggerFactory.getLogger(PrintPDFTable.class);

    private BaseFont baseFont;
    private Font tatalFont;
    private Font font;
    private Font font1;
    public PrintPDFTable() throws IOException, DocumentException {
        baseFont = BaseFont.createFont(Constants.FONT_PATH,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
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
     * 打印表格   正常单据打印
     * @param billHeaderDTO
     * @param document
     * @throws DocumentException
     */
    public void printTable(BillHeaderDTO billHeaderDTO, Document document) throws DocumentException {
        printTatle(billHeaderDTO,billHeaderDTO.getCostDeductionDOS(),document,1);
        printFoot(billHeaderDTO,document);
        document.add(new Paragraph("    "));
        document.add(new Paragraph("  ------------------------------------------------------------------------------------------------------------------------------"));
        document.add(new Paragraph("    "));
        printTatle(billHeaderDTO,billHeaderDTO.getCostDeductionDOS(),document,0);
        printFoot(billHeaderDTO,document);
    }

    /**
     * 三通费单据打印通道
     * @param billHeaderDTO
     * @param document
     * @throws DocumentException
     */
//    public void printThreeWayFeeTable(BillHeaderDTO billHeaderDTO, Document document) throws DocumentException {
//        printTatle(billHeaderDTO,billHeaderDTO.getCostDeductionDOS(),document,1);
//        printFoot(billHeaderDTO,document);
//        document.add(new Paragraph("    "));
//        document.add(new Paragraph("  ------------------------------------------------------------------------------------------------------------------------------"));
//        document.add(new Paragraph("    "));
//        printTatle(billHeaderDTO,billHeaderDTO.getCostDeductionDOS(),document,0);
//        printFoot(billHeaderDTO,document);
//    }

    /**
     * 打印表头
     * @param billHeaderDTO
     * @param document
     * @param i
     * @throws DocumentException
     */
    private void printTatle(BillHeaderDTO billHeaderDTO,ArrayList<CostDeductionDO> costDeductionDOS,Document document,int i) throws DocumentException {
        Paragraph para = new Paragraph(billHeaderDTO.getTitle(),tatalFont);
        //设置该段落为居中显示
        para.setAlignment(1);
        document.add(para);
        document.add(new Paragraph("   "));
        font.setSize(9F);
        font.setStyle(Font.BOLD);
//        document.add(new Paragraph("                                                                                                         抄表日期:"+tableEntity.getDoDate(),font));
        document.add(new Paragraph("       大楼名称:"+billHeaderDTO.getBuildingName()+"                                                               打印日期:"+billHeaderDTO.getRealGenerationTime(),font));
        document.add(new Paragraph("        ",font));
        printHeard(billHeaderDTO,document);

        ArrayList<BillSubjectDTO> billSubjectDTOS=billHeaderDTO.getBillSubjectDTOS();
        ArrayList<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOS = billHeaderDTO.getParkingSpaceCostDetailDOS();
        if (billSubjectDTOS.size()>0){
            printTatle(document);
            for (BillSubjectDTO billSubjectDTO:billSubjectDTOS) {
                printPrice(billSubjectDTO,document);
            }
        }
        if (parkingSpaceCostDetailDOS.size()>0){
            printParkingSpaceFee(document);
            for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOS) {
                printParkingSpaceFeeData(parkingSpaceCostDetailDO,document);
            }
        }

        if (costDeductionDOS.size()>0){
            printCodeDeduction(document);
            for (CostDeductionDO costDeductionDO:costDeductionDOS) {
                printCostDeductionData(costDeductionDO,document);
            }
        }
        printTotal(billHeaderDTO,document);
        printBigSum(billHeaderDTO.getActualTotalMoneyCollection(),document);

        printRemarks(billHeaderDTO,document);
        printFoot(document,i,billHeaderDTO);
    }

    /**
     * 打印列名
     * @param document
     * @throws DocumentException
     */
    private  void printTatle(Document document) throws DocumentException {
        PdfPTable table1 = new PdfPTable(9);
        table1.setTotalWidth(new float[] {2.6F,1.1F,0.9F,1F,2.7F,1F,1F,1.1F,1.1F});
        table1.addCell(getCell("       项目名称"));
//        table1.addCell(getCell("建筑面积"));
        table1.addCell(getCell("标准单价"));
        table1.addCell(getCell("月/张数"));
        table1.addCell(getCell("折扣率"));
        table1.addCell(getCell("     计费期间"));
        table1.addCell(getCell("使用量"));
        table1.addCell(getCell("应缴人"));
//        table1.addCell(getCell("是否欠费"));
        table1.addCell(getCell("应缴金额"));
        table1.addCell(getCell("实缴金额"));
        table1.setWidthPercentage(90);
        document.add(table1);
    }

    /**
     * 打印抵扣项目
     */
    private void printCodeDeduction(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(new float[] {1F,1F,1F,1F,1F,1F,3F});
        table.addCell(getCell("  抵扣项目"));
        table.addCell(getCell("  抵扣方式"));
        table.addCell(getCell(" 可抵扣金额"));
        table.addCell(getCell(" 已抵扣金额"));
        table.addCell(getCell("剩余可抵扣金额"));
        table.addCell(getCell(" 本次抵扣金额"));
        table.addCell(getCell("               抵扣记录"));
        table.setWidthPercentage(90);
        document.add(table);
    }


    /**
     * 打印车位费
     */
    private void printParkingSpaceFee(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(new float[] {1.5F,1F,1F,1F,1F,1F,2.5F,1F,1F});
        table.addCell(getCell("合同编号"));
        table.addCell(getCell("项目名称"));
        table.addCell(getCell("标准单价"));
        table.addCell(getCell("缴费月数"));
        table.addCell(getCell(" 折扣率"));
        table.addCell(getCell("车牌号"));
        table.addCell(getCell("        计费期间"));
        table.addCell(getCell("应收金额"));
        table.addCell(getCell("实收金额"));
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印车位费
     */
    private void printParkingSpaceFeeData(ParkingSpaceCostDetailDO parkingSpaceCostDetailDO,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(new float[] {1.5F,1F,1F,1F,1F,1F,2.5F,1F,1F});
        table.addCell(getCell(parkingSpaceCostDetailDO.getContractNumber()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getChargeName()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getChargeStandard().toString()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getDatedif().toString()));
        if (10d == parkingSpaceCostDetailDO.getDiscount()){
            table.addCell(getCell("不打折"));
        }else {
            table.addCell(getCell(parkingSpaceCostDetailDO.getDiscount()+"折"));
        }
        table.addCell(getCell(parkingSpaceCostDetailDO.getLicensePlateNumber()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getStartTime()+"至"+parkingSpaceCostDetailDO.getDueTime()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getAmountReceivable().toString()));
        table.addCell(getCell(parkingSpaceCostDetailDO.getActualMoneyCollection().toString()));
        table.setWidthPercentage(90);
        document.add(table);
    }




    /**
     * 打印表格尾部
     * @param document
     * @param i
     * @throws DocumentException
     */
    private void printFoot(Document document,int i,BillHeaderDTO billHeaderDTO) throws DocumentException {
        PdfPTable headerTable =new PdfPTable(2);
        headerTable.setWidthPercentage(90);
        PdfPTable iTable =new PdfPTable(2);
        PdfPCell iCell;
        PdfPCell cell;
        iTable =new PdfPTable(1);
        iCell = getCell("     客户(签字):"+billHeaderDTO.getSurname()+"                                                       经办人(签字):"+billHeaderDTO.getTollCollectorName());
        iCell.setVerticalAlignment(Element.ALIGN_CENTER);
        iCell.setFixedHeight(20F);
        iCell.setColspan(1);
        iTable.addCell(iCell);
        String msg="";
        if(i==1){
            msg="此为物业留存";
        }else {
            msg="此为业主留存";
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
     * @param billHeaderDTO
     * @param document
     * @throws DocumentException
     */
    private void printRemarks(BillHeaderDTO billHeaderDTO,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);

        PdfPCell iCell;
        iCell = getCell("备注: "+billHeaderDTO.getRemark());
        iCell.setVerticalAlignment(Element.ALIGN_CENTER);
        iCell.setFixedHeight(30F);
        iCell.setColspan(1);
        table.addCell(iCell);
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印小写金额总计
     * @param billHeaderDTO
     * @param document
     * @throws DocumentException
     */
    private void printTotal(BillHeaderDTO billHeaderDTO,Document document) throws DocumentException {
//        BigDecimal bigDecimal=new BigDecimal(doSum);
//        String sum= NumberToCN.number2CNMontrayUnit(bigDecimal);
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[] {1.7F,5.3F,1F,1F});
        table.addCell(getCell("   合计小写金额"));
        table.addCell(getCell(""));
        table.addCell(getCell(billHeaderDTO.getAmountTotalReceivable().toString()));
        table.addCell(getCell(billHeaderDTO.getActualTotalMoneyCollection().toString()));
        table.setWidthPercentage(90);
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
        table.setTotalWidth(new float[] {90F,385F});
        table.addCell(getCell("   合计金额大写"));
        table.addCell(getCell(sum));
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印金额小写的行
     * @param mothSum
     * @param lastSum
     * @param document
     * @throws DocumentException
     */
    private void printSum(double mothSum,double lastSum,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[] {398F,35F,35F,35F});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(getCell("合计金额小写:"));
        table.addCell(getCell(mothSum+""));
        table.addCell(getCell(lastSum+""));
//        table.addCell(getCell(doSum+""));
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
     * @param billSubjectDTO
     * @param document
     * @throws DocumentException
     */
    private void printPrice(BillSubjectDTO billSubjectDTO, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(new float[] {2.6F,1.1F,0.9F,1F,2.7F,1F,1F,1.1F,1.1F});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        String chargeName = billSubjectDTO.getChargeName();
        if (chargeName==null){
            chargeName="";
        }
        PdfPCell cell=getCell(isNull(chargeName));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        if (billSubjectDTO.getChargeStandard()==null){
            cell = getCell(isNull(""));
        }else {
            cell=getCell(isNull(billSubjectDTO.getChargeStandard()));
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        if (billSubjectDTO.getDatedif()==null){
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getDatedif().toString())));
        }
        if (billSubjectDTO.getDiscount()==null){
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull("")));
        }else {
            if (10d==billSubjectDTO.getDiscount()){
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(getCell(isNull("不打折")));
            }else {
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(getCell(isNull(billSubjectDTO.getDiscount()+"折")));
            }
        }
        if (billSubjectDTO.getBillingPeriod()==null){
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getBillingPeriod())));
        }

        if (billSubjectDTO.getUsageAmount()==null){
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getUsageAmount().toString())));
        }
        if (billSubjectDTO.getPayableParty()==null) {
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getPayableParty())));
        }

//        if (billSubjectDTO.getStateOfArrears()==null) {
//            table.addCell(getCell(isNull("")));
//        }else {
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_CENTER);
//            table.addCell(getCell(isNull(billSubjectDTO.getStateOfArrears())));
//        }
        if (billSubjectDTO.getAmountReceivable()==null){
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getAmountReceivable().toString())));
        }
        if (billSubjectDTO.getActualMoneyCollection()==null) {
            table.addCell(getCell(isNull("")));
        }else {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCell(isNull(billSubjectDTO.getActualMoneyCollection().toString())));
        }
        table.setWidthPercentage(90);
        document.add(table);
    }


    private void printCostDeductionData(CostDeductionDO costDeductionDO,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(new float[] {1F,1F,1F,1F,1F,1F,3F});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell=getCell(isNull(costDeductionDO.getDeductionItem()));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        if (null==costDeductionDO.getDeductibleMoney()){
            cell = getCell(isNull(""));
        }else {
            cell = getCell(isNull(costDeductionDO.getDeductibleMoney()));//可抵扣金额
        }
        table.addCell(cell);
        if (null==costDeductionDO.getDeductionMethod()){
            cell = getCell(isNull(""));
        }else {
            cell = getCell(isNull(costDeductionDO.getDeductionMethod()));//抵扣方式
        }
        table.addCell(cell);
        cell = getCell(costDeductionDO.getDeductibledMoney().toString());//已抵扣金额
        table.addCell(cell);
        cell = getCell(costDeductionDO.getSurplusDeductibleMoney().toString());//剩余可抵扣金额
        table.addCell(cell);
        cell = getCell(costDeductionDO.getAmountDeductedThisTime().toString());//本次抵扣金额
        table.addCell(cell);
        if ("0".equals(costDeductionDO.getDeductionRecord())){
            costDeductionDO.setDeductionRecord(null);
        }
        if (null==costDeductionDO.getDeductionRecord()){
            cell = getCell(isNull(""));
        }else {
            cell = getCell(isNull(costDeductionDO.getDeductionRecord()));//抵扣记录
        }
        table.addCell(cell);
        table.setWidthPercentage(90);
        document.add(table);
    }

    /**
     * 打印表格头部的数据
     * @param billHeaderDTO
     * @param document
     * @throws DocumentException
     */
    private void printHeard(BillHeaderDTO billHeaderDTO,Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        font1.setSize(8F);
        table.setTotalWidth(new float[] {0.6F,1.5F,0.8F,1F,0.6F,1F,1F,1F});

        Paragraph para = new Paragraph("房间代码",font1);
        para.setAlignment(1);
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(para);
        table.addCell(cell);
        table.addCell(new Paragraph(billHeaderDTO.getRoomCode(),font1));
        table.addCell(new Paragraph("客户名称",font1));
        table.addCell(new Paragraph(billHeaderDTO.getSurname(),font1));
        table.addCell(new Paragraph("客户电话",font1));
        table.addCell(new Paragraph(billHeaderDTO.getMobilePhone(),font1));
        table.addCell(new Paragraph("物业费到期时间",font1));
        table.addCell(new Paragraph(billHeaderDTO.getDueTime(),font1));

        table.addCell(new Paragraph("建筑面积",font1));
        table.addCell(new Paragraph(billHeaderDTO.getRoomSize().toString(),font1));
        table.addCell(new Paragraph("支付金额",font1));
        double actualMoney = billHeaderDTO.getActualTotalMoneyCollection();
        table.addCell(new Paragraph(String.valueOf(actualMoney),font1));
        table.addCell(new Paragraph("预存金额",font1));
        table.addCell(new Paragraph(billHeaderDTO.getSurplus().toString(),font1));
        table.addCell(new Paragraph("",font1));
        table.addCell(new Paragraph("",font1));
//        if (null!=billHeaderDTO.getDeductibleSurplus()){
//            if (-1d==billHeaderDTO.getDeductibleSurplus()){
//                table.addCell(new Paragraph("",font1));
//                table.addCell(new Paragraph("",font1));
//            }else {
//                table.addCell(new Paragraph("抵扣剩余",font1));
//                table.addCell(new Paragraph(billHeaderDTO.getDeductibleSurplus().toString(),font1));
//            }
//        }
        table.setWidthPercentage(90);
        document.add(table);

    }

    /**
     * 把两张表分割开
     * @param document
     * @throws DocumentException
     */
    private void printFoot(BillHeaderDTO billHeaderDTO,Document document) throws DocumentException {
        document.add(new Paragraph("   ",tatalFont));
        int count = 0;
        String str = "";
        String str1 = "               ";
        if (count==0){
            for (int i=0;i<58-(billHeaderDTO.getOrganizationId().length()-7);i++){
                str+=" ";
            }
        }
        count++;
        Paragraph para = new Paragraph(str+billHeaderDTO.getOrganizationId(),tatalFont);
        para.setAlignment(0);
        document.add(para);
        Paragraph para2 = new Paragraph(DateUtil.date("yyyy")+"年"+DateUtil.date("MM")+"月"+DateUtil.date("dd")+"日"+str1,tatalFont);
        para2.setAlignment(2);
        document.add(para2);
    }
}
