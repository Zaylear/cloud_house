package com.rbi.interactive.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelPOI {

    //读取excel
    public static Workbook readExcel(MultipartFile picFile){
        Workbook wb = null;
        if(picFile.isEmpty()){
            return null;
        }
//        String extString = filePath.substring(filePath.lastIndexOf("."));
        int begin = picFile.getOriginalFilename().indexOf(".");
        int last = picFile.getOriginalFilename().length();
        //获得文件后缀名
        String extString = picFile.getOriginalFilename().substring(begin, last);

        InputStream is = null;
        try {
            is = picFile.getInputStream();
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return "";
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
//                case Cell.CELL_TYPE_NUMERIC:{
//                    cellValue = String.valueOf(cell.getNumericCellValue());
//                    break;
//                }
//                case Cell.CELL_TYPE_FORMULA:{
//                    //判断cell是否为日期格式
//                    if(DateUtil.isCellDateFormatted(cell)){
//                        //转换为日期格式YYYY-mm-dd
//                        cellValue = cell.getDateCellValue();
//                    }else{
//                        //数字
//                        cellValue = String.valueOf(cell.getNumericCellValue());
//                    }
//                    break;
//                }
//                case Cell.CELL_TYPE_STRING:{
//                    cellValue = cell.getRichStringCellValue().getString();
//                    break;
//                }
//                default:
//                    cellValue = "";
                case Cell.CELL_TYPE_NUMERIC: //数字
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING: //字符串
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN: //Boolean
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: //公式
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_BLANK: //空值
                    cellValue = "";
                    break;
                case Cell.CELL_TYPE_ERROR: //故障
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
