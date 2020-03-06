package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.EventRecordDO;
import com.rbi.interactive.service.EventService;
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

@RequestMapping("/event")
@RestController
public class EventController {

    private final static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    EventService eventService;

    /**
     * 事件分页查询
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findByPage")
    public ResponseVo findEventRecordByPage(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            int pageNo = jsonObject.getInteger("pageNo");
            int pageSize = jsonObject.getInteger("pageSize");
            PageData<EventRecordDO> billDOPageData = eventService.findByPage(userId,pageNo,pageSize);
            return ResponseVo.build("1000","请求成功",billDOPageData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
