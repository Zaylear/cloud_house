package com.rbi.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.edo.ChargeItemDO;
import com.rbi.admin.service.ChargeItemService;
import com.rbi.admin.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping(value = "/charge")
public class ChargeItemController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeItemController.class);

    @Autowired
    ChargeItemService chargeItemService;


    @RequestMapping(value = "/findAll",method = RequestMethod.POST)
    public ResponseVo<List<ChargeItemDO>>findAll(@RequestBody JSONObject request){
        try {
            String organizationId = request.getString("organizationId");
            int enable2 = 1;
            List<ChargeItemDO> chargeItemDOS = chargeItemService.findByOrganizationIdAndEnable(organizationId,enable2);

            return ResponseVo.build("1000","查询成功",chargeItemDOS);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }



}
