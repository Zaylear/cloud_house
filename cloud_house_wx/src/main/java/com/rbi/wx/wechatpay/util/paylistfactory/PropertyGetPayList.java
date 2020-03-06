package com.rbi.wx.wechatpay.util.paylistfactory;

import com.rbi.wx.wechatpay.dto.paymentlist.PayMentListPropertyDTO;
import com.rbi.wx.wechatpay.mapper.PayMentMapper;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.util.chargereal.chargerealproperty.RoomPerproty;
import com.rbi.wx.wechatpay.util.paymentlistutil.GetPayMentListJson;

import java.util.List;

public class PropertyGetPayList implements GetPayList{
    @Override
    public JsonEntityUtil getPayList(String roomCode,String organizationId, String chargeCode, PayMentMapper payMentMapper) {
        JsonEntityUtil jsonEntityUtil=null;
        List<PayMentListPropertyDTO> listPropertyDTO=payMentMapper.findPropertyList(chargeCode);
        RoomPerproty roomPerproty=payMentMapper.findRoomByPerproty(roomCode,organizationId);
        GetPayMentListJson getPayMentListJson=new GetPayMentListJson();
        List list=getPayMentListJson.getJson(listPropertyDTO,roomPerproty);
        jsonEntityUtil = new JsonEntityUtil("1000", "成功");
        jsonEntityUtil.setEntity(list);
        return jsonEntityUtil;
    }
}
