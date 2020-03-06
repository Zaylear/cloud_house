package com.rbi.wx.wechatpay.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.User;
import com.rbi.wx.wechatpay.dto.UserIdentityEntity;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.AddTenantUserDTO;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.UpdateRoomCustomerRequestEntity;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.UpdateRoomUserRequestEntity;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.UpdateTenantDTO;

import java.util.List;

public interface RoomCustomerService {
    JsonEntityUtil deleteDeputy(String userId,String roomCode,String identity,String appKey,String verificationCode);
    JsonEntityUtil getRoomUser(String userId,String identity,String roomCode);
    JsonEntityUtil addUser(List<UpdateRoomCustomerRequestEntity> roomList, User user, UserIdentityEntity userIdentityEntity,String verificationCode,String appKey);
    JsonEntityUtil houseInfo(String roomCode,String organizationId,String userId);
    JsonEntityUtil getIndexRoom(String userId);
    JsonEntityUtil getIndexRoomUser(String UserIdAPPKEY,Integer identity,Integer PageSize,Integer pageNum);
    JsonEntityUtil getPermission(String userIdAppKey,String roomCode);
    JsonEntityUtil updateGetRoomCode(String userIdAppKey);
    JsonEntityUtil updateRoomUser(UpdateRoomUserRequestEntity updateRoomUserRequestEntity);
    JsonEntityUtil getRoomCodesIdentity(String userId,String identity);
    JsonEntityUtil getRoomCodes(String userIdAppKey);
    JsonEntityUtil getTenantuserRoomCode(String userId,String appKey);
    JsonEntityUtil addRoomUserInIndex(List<String> roomCode,String identity,String userId,String startDate);
    JsonEntityUtil roomUserSubmit(String userName,String userPhoen,String sex,String userId
                                 ,String verificationCode,String appKey,List<UpdateRoomCustomerRequestEntity> roomCodes
                                 ,String identity);
    JsonEntityUtil addTenantUser(AddTenantUserDTO addTenantUserDTO);
    JsonEntityUtil getTenantUser(String userId,String userIdAppKey);
    JsonEntityUtil updateTenant(UpdateTenantDTO updateTenantDTO);
    JsonEntityUtil getPorperty(String roomCode);
    JsonEntityUtil proprietorGetVerificationCode(String appKey);
}
