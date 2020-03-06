package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.UserInfo;
import com.rbi.wx.wechatpay.mapper.RoomMapper;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.service.RoomService;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private HTTPRequest httpRequest;

    /**
     * 获取自己的房间列表
     * @param request
     * @return
     */
    @Override
    public JsonEntityUtil findRoomByUserId(HttpServletRequest request) {
        String url= Constants.DATE_URL+"";
        JsonEntityUtil jsonEntityUtil=null;
        String header=request.getHeader("wx-requested-h5-request");
        UserInfo userInfo=(UserInfo) redisUtil.get(header);
        List codeList=this.roomMapper.findRoomByUserId(userInfo.getUserId());
        String dataStr= JSON.toJSONString(codeList);
        System.out.println(dataStr);
        String result=httpRequest.JsonRequest(url,"POST",dataStr);
        return jsonEntityUtil;
    }
}
