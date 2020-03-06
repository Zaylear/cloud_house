package com.rbi.wx.wechatpay.controller;

import com.rbi.wx.wechatpay.util.tableutil.CreateExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
@Controller
@Api(value = "生成excel",hidden = true)
public class ExcelController {
    /**
     * 下载exl
     * 弃用
     * @param response
     */
    public void getexcel(HttpServletResponse response){
        CreateExcel createExcel=new CreateExcel();
        createExcel.createTable(createExcel.get(),response);
    }
}
