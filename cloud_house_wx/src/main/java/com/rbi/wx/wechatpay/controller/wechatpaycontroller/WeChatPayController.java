package com.rbi.wx.wechatpay.controller.wechatpaycontroller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.houseinfo.RoomUser;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.service.pdfservice.PDFPossService;
import com.rbi.wx.wechatpay.service.wechatservice.WeChatPayService;
import com.rbi.wx.wechatpay.util.receipt.entity.TableEntity;
import com.rbi.wx.wechatpay.util.receipt.entity.TableJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WeChatPayController {

    @Autowired
    private WeChatPayService weChatPayService;
     @Autowired
     private PDFPossService pdfPossService;
    @RequestMapping("/success")  //支付成功调用
        public void success(HttpServletRequest request,HttpServletResponse response){
        System.out.println("收到通知");
        this.weChatPayService.success(response,request);
    }


    /**
     * 获取pdf的
     * 弃用
     * @param response
     */
    public void getpdf(HttpServletResponse response){
     this.pdfPossService.getDocument(response);
    }

    /**
     * 打印表格的
     * 弃用
     * @param jsonObject
     * @param response
     */
    public void printTable(@RequestBody JSONObject jsonObject,HttpServletResponse response){
        TableEntity tableEntity=JSON.toJavaObject(jsonObject,TableEntity.class);
        this.pdfPossService.printTable(tableEntity,response);
    }

    /**
     * 获取表单
     * 弃用
     * @param response
     */
    public void gettable(HttpServletResponse response){
        TableEntity tableEntity1=new TableEntity();
        tableEntity1.setRemark("222");
        tableEntity1.setDoDate("20190108");
        tableEntity1.setHouseName("22栋");
        tableEntity1.setPrintDate("20190801");
        tableEntity1.setUsrName("张三");
        tableEntity1.setHouseID("233333");
        TableJson tableJson=new TableJson();
        tableJson.setHouseSize("89.10");
        tableJson.setLastMonth("90.31");
        //tableJson.setLastMonthPrice(39.54);
        tableJson.setMonth("");
        tableJson.setMonthDosage("89.31");
        tableJson.setMonthPrice(39.31);
        tableJson.setName("水费");
        tableJson.setPrice(3.48);
        tableJson.setPriceDate("2019/04/01-2019/05/01");
        tableJson.setSum(34.56);
        //=======================
        TableJson tableJson2=new TableJson();
        tableJson2.setHouseSize("89.10");
        tableJson2.setLastMonth("90.31");
        tableJson2.setLastMonthPrice(39.54);
        tableJson2.setMonth("39.41");
        tableJson2.setMonthDosage("89.31");
        tableJson2.setMonthPrice(39.31);
        tableJson2.setName("物业费");
        tableJson2.setPrice(3.48);
        tableJson2.setPriceDate("2019/04/01-2019/05/01");
        tableJson2.setSum(34.56);
        //===============
        TableJson tableJson1=new TableJson();
        tableJson1.setHouseSize("89.10");
        tableJson1.setLastMonth("90.31");
        tableJson1.setLastMonthPrice(39.54);
        tableJson1.setMonth("39.41");
        tableJson1.setMonthDosage("89.31");
        tableJson1.setMonthPrice(39.31);
        tableJson1.setName("电费");
        tableJson1.setPrice(3.48);
        tableJson1.setPriceDate("2019/04/01-2019/05/01");
        tableJson1.setSum(34.56);
        ArrayList<TableJson> tableJsons=new ArrayList<>();
        tableJsons.add(tableJson);
        tableJsons.add(tableJson1);
        tableJsons.add(tableJson2);
        tableEntity1.setTaleList(tableJsons);
         this.pdfPossService.printTable(tableEntity1,response);
    }


}
