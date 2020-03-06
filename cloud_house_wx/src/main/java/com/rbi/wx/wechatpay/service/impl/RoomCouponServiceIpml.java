package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.RoomInfo;
import com.rbi.wx.wechatpay.mapper.RoomCouponMapper;
import com.rbi.wx.wechatpay.service.RoomCouponService;
import com.rbi.wx.wechatpay.util.Constants;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomCouponServiceIpml implements RoomCouponService{
    @Autowired
    private RoomCouponMapper roomCouponMapper;
    @Autowired
    private HTTPRequest httpRequest;

    /**
     * 获取欠费房间的列表
     * 欠费关联还未达成
     * @param userId
     * @return
     */
    @Override
    public JsonEntityUtil getRoomInfo(String userId) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
        this.roomCouponMapper.getRoomCode(userId);
        String jsonDate=httpRequest.JsonRequest(Constants.DATE_URL+"","POST","");
        JSONObject jsonObject= JSON.parseObject(jsonDate);
        JSONObject dataObeject=jsonObject.getJSONObject("data");
        List<RoomInfo> list=JSON.parseArray(dataObeject.toJSONString(),RoomInfo.class);
        jsonEntityUtil=new JsonEntityUtil("10000","允许访问");
        jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("10003","服务器繁忙");
        }
        return jsonEntityUtil;
    }
}
