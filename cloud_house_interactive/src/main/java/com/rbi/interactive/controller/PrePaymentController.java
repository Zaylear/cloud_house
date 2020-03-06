package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.service.PrePaymentService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/prePayment")
public class PrePaymentController {

    private final static Logger logger = LoggerFactory.getLogger(PrePaymentController.class);

    @Autowired
    PrePaymentService prePaymentService;

    /**
     * 分页查询预缴记录
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findPrePaymentByPageAndUserIdDesc")
    public ResponseVo<PageData> findPrePaymentByPageAndUserIdDesc(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            PageData pageData = prePaymentService.findPrePaymentByPageAndUserIdDesc(pageNo,pageSize,userId);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (Exception e) {
            logger.error("【预缴记录请求类】服务器处理异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
