package com.rbi.wx.wechatpay.util.tableutil;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateExcel {
    /**
     * 创建EXCEL表格
     * @param excelEntity
     */
    public void createTable(ExcelEntity excelEntity, HttpServletResponse response){
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        HSSFSheet sheet=hssfWorkbook.createSheet("testsheet");
        craeteHeard(excelEntity.getColumName(),sheet);
        List<HashMap<String,String>> list=excelEntity.getColumValue();
        int i=1;
        for (HashMap hashMap:list){
            insertValue(excelEntity.getColumName(),hashMap,sheet,i);
            i++;
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", "test.xls"));
        try {
            hssfWorkbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 数据测试
     */
    public ExcelEntity get(){
        HashMap<String,String> columName=new HashMap<>();
        List<HashMap<String,String>> columValue=new ArrayList<>();
        ExcelEntity excelEntity=new ExcelEntity();
        columName.put("1","1");
        columName.put("2","2");
        columName.put("3","3");
        columName.put("0","0");
        HashMap<String,String> hashMap=new HashMap<>();
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap.put("1","11");
        hashMap.put("2","12");
        hashMap.put("3","13");
        hashMap.put("0","10");
        hashMap1.put("1","21");
        hashMap1.put("2","22");
        hashMap1.put("3","23");
        columValue.add(hashMap);
        columValue.add(hashMap1);
        excelEntity.setColumName(columName);
        excelEntity.setColumValue(columValue);
        return excelEntity;
    }

    /**
     * 创建列名
     */
    private void craeteHeard(HashMap hashMap,HSSFSheet sheet){
        HSSFRow row=sheet.createRow(0);
        for (int i=0;i<hashMap.size();i++){
            row.createCell(i).setCellValue(hashMap.get(i+"").toString());
        }
    }

    /**
     * 向表格里面插入数值
     */
    private void insertValue(HashMap heardMap,HashMap valueMap,HSSFSheet sheet,int colum){
        HSSFRow row=sheet.createRow(colum);
        for (int i=0;i<heardMap.size();i++){
            if(valueMap.get(heardMap.get(i+""))!=null){
                row.createCell(i).setCellValue(valueMap.get(heardMap.get(i+"")).toString());
            }else {
                row.createCell(i).setCellValue("");
            }
        }
    }
}
