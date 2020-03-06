package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.entity.BillDetailedDO;
import com.rbi.interactive.service.CostSplitService;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/cash/register")
public class CostSplitController {

    public final static Logger logger = LoggerFactory.getLogger(CostSplitController.class);

    @Autowired
    CostSplitService costSplitService;

    @PostMapping("/cost_split")
    public ResponseVo costSplitController(@RequestBody JSONObject jsonObject){
        try {
            List<BillDetailedDO> billDetailedDOS = costSplitService.costSplit(jsonObject);
            return ResponseVo.build("1000","请求成功",billDetailedDOS);
        }catch (DueTimeException e) {
            logger.error("【费用拆分请求类】到期时间输入异常，ERROR：{}",e);
            return ResponseVo.build("1002","缴费结束时间输入异常！");
        }catch (Exception e){
            logger.error("【费用拆分请求类】费用拆分异常，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
