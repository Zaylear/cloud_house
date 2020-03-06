package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.rbi.common.interactive.PropertyFeeDueTime;
import com.rbi.wx.wechatpay.dto.roomcusomer.RoomTimeDTO;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.RoomInfo;
import com.rbi.wx.wechatpay.dto.UserIdentityEntity;
import com.rbi.wx.wechatpay.dto.houseinfo.RoomUser;
import com.rbi.wx.wechatpay.dto.RoomUserDTO;
import com.rbi.wx.wechatpay.dto.User;
import com.rbi.wx.wechatpay.dto.houseinfo.HouseInfoDTO;
import com.rbi.wx.wechatpay.dto.houseinfo.UserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.IndexRoomDTO;
import com.rbi.wx.wechatpay.mapper.RoomCustomerMapper;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.*;
import com.rbi.wx.wechatpay.service.RoomCustomerService;
import com.rbi.wx.wechatpay.util.DateUtil;
import com.rbi.wx.wechatpay.util.HTTPRequest;
import com.rbi.wx.wechatpay.util.Tools;
import com.rbi.wx.wechatpay.util.msgutil.SendSMSUtil;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RoomCustomerServiceImpl implements RoomCustomerService{
    @Autowired
    private RoomCustomerMapper roomCustomerMapper;
    @Autowired
    private HTTPRequest httpRequest;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Reference
    private PropertyFeeDueTime propertyFeeDueTime;
    @Override
    /**
     * 删除房间用户之间的关联
     */
    public JsonEntityUtil deleteDeputy(String userId, String roomCode,String identity,String appKey,String verificationCode) {
        JsonEntityUtil jsonEntityUtil=null;
        try {

            this.redisUtil.setRedisDb(1);
            String propertyId=this.rsaGetStringUtil.getPrivatePassword(appKey);
            String propertyPhone=this.roomCustomerMapper.getPhoneByUserId(propertyId);
            String redisVerificationCode=this.redisUtil.get("msg"+propertyPhone).toString();
            if (redisVerificationCode==null||redisVerificationCode.equals("")){
                return  jsonEntityUtil=new JsonEntityUtil("1002","验证码错误");
            }
            if (!redisVerificationCode.equals(verificationCode)){
                return  jsonEntityUtil=new JsonEntityUtil("1002","验证码错误");
            }


            this.roomCustomerMapper.delete(userId,roomCode,identity);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }


        return jsonEntityUtil;
    }

    /**
     * 通过roomcode和userId获取数据
     * 获取个人页面租客或者副业主的信息
     * @param userIdAppKey
     * @return
     */
    @Override
    public JsonEntityUtil getRoomUser(String userIdAppKey,String identity,String roomCode) {

        JsonEntityUtil jsonEntityUtil=null;
        List<String> roomCodeList=new LinkedList<>();
        try {
            if (roomCode==null){
                String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
                roomCodeList=this.roomCustomerMapper.getRoomCode(userId);
            }else {
                roomCodeList.add(roomCode);
            }
            List<RoomUser> responseList=this.roomCustomerMapper.getRoomUser(roomCodeList,identity);
            List<RoomInfo> result=new LinkedList<>();
            for(RoomUser roomUser:responseList){
                 result.add(this.roomCustomerMapper.finRoomUserByCode(roomUser.getRoomCode(),roomUser.getUserId(),identity));
            }
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(result);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    /**
     * 修改添加新的关联
     * @param roomList
     * @param user
     * @return
     */
    @Override
    public JsonEntityUtil addUser(List<UpdateRoomCustomerRequestEntity> roomList, User user, UserIdentityEntity userIdentityEntity,String verificationCode,String appKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            //业主userId
            this.redisUtil.setRedisDb(1);
            String propertyId=this.rsaGetStringUtil.getPrivatePassword(appKey);
            String propertyPhone=this.roomCustomerMapper.getPhoneByUserId(propertyId);
            String redisVerificationCode=this.redisUtil.get("msg"+propertyPhone).toString();
            if (redisVerificationCode==null||redisVerificationCode.equals("")){
                return  jsonEntityUtil=new JsonEntityUtil("1002","验证码错误");
            }
            if (!redisVerificationCode.equals(verificationCode)){
                return  jsonEntityUtil=new JsonEntityUtil("1002","验证码错误");
            }
            String userId=this.roomCustomerMapper.userIsNullFindByIdNumber(user.getIdNumber());
            user.setUserId(userId);
            if (userId==null){
                userId= DateUtil.timeStamp()+ Tools.random(10,99);
                Integer sex=user.getSex().equals("男")?1:2;
               this.roomCustomerMapper.addUser(userId,sex+"",user.getMobilePhone(),user.getRealName(),user.getIdNumber());
            }else {
                Integer sex=user.getSex().equals("男")?1:2;
                this.roomCustomerMapper.updateUser(user.getUserId(),sex+"",user.getMobilePhone(),user.getRealName(),user.getIdNumber());
            }
            userId=this.roomCustomerMapper.userIsNullFindByIdNumber(user.getIdNumber());
            List<String> trueRoomCodes=this.roomCustomerMapper.getTrueRoomCodes(userId,userIdentityEntity.getIdentity()+"");

            roomList.removeAll(trueRoomCodes);
            for (UpdateRoomCustomerRequestEntity roomCode:roomList){
                if (roomCode.getEndTime().equals("")){
                    roomCode.setEndTime(null);
                }
                if (roomCode.getStartTime().equals("")){
                    roomCode.setStartTime(null);
                }

                if (userIdentityEntity.getIdentity()==2){
                    RoomTimeDTO  roomTimeDTO=this.roomCustomerMapper.getRoomTimeInfo(roomCode.getRoomCode(),roomCode.getOrganizationId());
                    this.roomCustomerMapper.addUserRoomCustomer2(roomCode,userId,userIdentityEntity,roomTimeDTO);
                }else {
                    this.roomCustomerMapper.addUserRoomCustomer3(roomCode,userId,userIdentityEntity);
                }
}
            jsonEntityUtil=new JsonEntityUtil("1000","提交完成");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","出现错误");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil houseInfo(String roomCode,String organizationId,String userIdAppKey) {
       JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            HouseInfoDTO<UserDTO,RoomUserDTO> result=new HouseInfoDTO<UserDTO,RoomUserDTO>();
            RoomUserDTO roomUserDTO=this.roomCustomerMapper.findRoomUser(roomCode,organizationId);
            List chargeMaxTime=new ArrayList();
                    chargeMaxTime.add(this.propertyFeeDueTime.propertyFeeDueTime(roomCode,organizationId));
            result.setHouseInfo(roomUserDTO);
            result.setChargeMaxTime(chargeMaxTime!=null&&chargeMaxTime.size()>0?chargeMaxTime:null);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(result);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    /**
     * 获取房屋主页的信息
     * @param userIdAppKey
     * @return
     */
    @Override
    public JsonEntityUtil getIndexRoom(String userIdAppKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List list=this.roomCustomerMapper.getIndexRoomCode(userId);
            if(list.size()==0||list==null){
                List<IndexRoomDTO> listJsonData=new LinkedList<>();
                jsonEntityUtil=new JsonEntityUtil("1000","没有信息");
                jsonEntityUtil.setEntity(listJsonData);
                return jsonEntityUtil;
            }
            List<IndexRoomDTO> listJsonData=this.roomCustomerMapper.findIndexRoomByroomCodes(list);
            for (IndexRoomDTO indexRoomDTO:listJsonData){
                indexRoomDTO.setStatus(this.roomCustomerMapper.getStatus(indexRoomDTO.getRoomCode()));
            }
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(listJsonData);
        }catch (Exception e){
            e.printStackTrace();
          jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    /**
     * 个人主页的接口信息
     * @param userIdAppKey
     * @param identity
     * @param PageSize
     * @param pageNum
     * @return
     */
    @Override
    public JsonEntityUtil getIndexRoomUser(String userIdAppKey, Integer identity, Integer PageSize, Integer pageNum) {
        JsonEntityUtil jsonEntityUtil=null;
        List<String> roomCodeList=new LinkedList<>();
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            roomCodeList=this.roomCustomerMapper.getRoomCode(userId);
            List<RoomInfo> result=new LinkedList<>();
            if (roomCodeList==null||roomCodeList.size()<1){
                jsonEntityUtil =new JsonEntityUtil("1000","成功");
                jsonEntityUtil.setEntity(result);
                return jsonEntityUtil;
            }
            List<String> responseList=this.roomCustomerMapper.getuserIds(roomCodeList,identity+"");

            if (responseList==null||responseList.size()<1){
                jsonEntityUtil =new JsonEntityUtil("1000","成功");
                jsonEntityUtil.setEntity(result);
            }else {
             result=this.roomCustomerMapper.findRoomUserList(responseList,identity,5,(pageNum-1)*5);
            }
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(result);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPermission(String userIdAppKey, String roomCode) {
        JsonEntityUtil jsonEntityUtil=null;

        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List list=this.roomCustomerMapper.findRoomPermission(roomCode,userId);
            if (list.size()>0){
                jsonEntityUtil =new JsonEntityUtil("1000","成功");
            }else {
                jsonEntityUtil =new JsonEntityUtil("1003","房间有误");
            }

        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil updateGetRoomCode(String userIdAppKey) {
        JsonEntityUtil jsonEntityUtil=null;

        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List list=this.roomCustomerMapper.findRoomCodes(userId);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil updateRoomUser(UpdateRoomUserRequestEntity updateRoomUserRequestEntity) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            Integer status=this.roomCustomerMapper.updateUserInfo(updateRoomUserRequestEntity.getUserId(),
                    updateRoomUserRequestEntity.getUserPhone(),
                    updateRoomUserRequestEntity.getSex(),
                    updateRoomUserRequestEntity.getUserName().substring(0,1));
            System.out.println(status);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
           // Integer updateStatus=this.roomCustomerMapper.addRoomUser(updateRoomUserRequestEntity.getRoomCodes(),updateRoomUserRequestEntity.getUserId(),updateRoomUserRequestEntity.getIdentity());
           // System.out.println(updateStatus);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getRoomCodesIdentity(String userId, String identity) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            List roomCodes=this.roomCustomerMapper.getRoomCodesIdentity(userId, identity);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomCodes);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getRoomCodes(String userIdAppKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List roomCodes=this.roomCustomerMapper.getRoomCodesIdentity(userId,"1");
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomCodes);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil addRoomUserInIndex(List<String> roomCode, String identity, String userId, String startDate) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            if (startDate.equals("-1")){
                this.roomCustomerMapper.addRoomUserInIndex(null,identity,userId,roomCode);
            }else {
                this.roomCustomerMapper.addRoomUserInIndex(startDate,identity,userId,roomCode);
            }
            jsonEntityUtil =new JsonEntityUtil("1000","成功");

        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil roomUserSubmit(String userName, String userPhoen, String sex, String userId, String verificationCode, String appKey, List<UpdateRoomCustomerRequestEntity> roomCodes, String identity) {

        JsonEntityUtil jsonEntityUtil=null;
        try {
            //业主userId
            this.redisUtil.setRedisDb(1);
            String propertyId=this.rsaGetStringUtil.getPrivatePassword(appKey);
            String propertyPhone=this.roomCustomerMapper.getPhoneByUserId(propertyId);
            this.roomCustomerMapper.updateUserInfoByUserId(userPhoen,sex.equals("男")?"1":"2",userId,userName);
            List<String> trueRoomCodes=this.roomCustomerMapper.getTrueRoomCodes(userId,identity);
            List<String> UpdateRoomCodes=new ArrayList<>();
            List<String> deleteRoomCode=new ArrayList<>();
            List<UpdateRoomCustomerRequestEntity> insertRoomCode=new ArrayList<>();
            /**
             * 取出删除的部分
             */
            for (String roomCode:trueRoomCodes){
                boolean notHave=true;
                for (UpdateRoomCustomerRequestEntity updateRoomCustomerRequestEntity:roomCodes){
                    if (updateRoomCustomerRequestEntity.getRoomCode().equals(roomCode)){
                        notHave=false;
                    }
                }
                if (notHave){
                    deleteRoomCode.add(roomCode);
                }
            }

            /**
             * 取出新增部分
             */
            for(UpdateRoomCustomerRequestEntity updateRoomCustomerRequestEntity:roomCodes)
            {
                boolean notHave=true;
                for (String roomCode:trueRoomCodes){
                    if (roomCode.equals(updateRoomCustomerRequestEntity.getRoomCode())){
                        notHave=false;
                    }
                }
                if (notHave){
                    insertRoomCode.add(updateRoomCustomerRequestEntity);
                }
            }

            this.roomCustomerMapper.updateUserInfo(userId,userPhoen,sex.equals("男")?"1":"2",userName);
            if (insertRoomCode.size()>0){
                for (UpdateRoomCustomerRequestEntity u:insertRoomCode){
                    RoomTimeDTO roomTimeDTO=this.roomCustomerMapper.getRoomTimeInfo(u.getRoomCode(),u.getOrganizationId());
                    if (identity.equals("2")){
                        this.roomCustomerMapper.insertNewRoomCustomer2(userId,u,identity,roomTimeDTO);
                    }else {
                        this.roomCustomerMapper.insertNewRoomCustomer3(userId,u,identity);
                    }
                }

            }

            for (String deleteRoom:deleteRoomCode){
                this.roomCustomerMapper.deleteRoomCustomer(userId,identity,deleteRoom);
            }
            jsonEntityUtil=new JsonEntityUtil("1000","提交完成");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","出现错误");
        }
        return jsonEntityUtil;

//        JsonEntityUtil jsonEntityUtil=null;
//        try {
//            this.roomCustomerMapper.updateUserInfoByUserId(userPhoen,sex.equals("男")?"1":"2",userId,userName);
//            jsonEntityUtil =new JsonEntityUtil("1000","成功");
//        }catch (Exception e){
//            e.printStackTrace();
//            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
//        }
//        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil addTenantUser(AddTenantUserDTO addTenantUserDTO) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.roomCustomerMapper.userIsNullFindByPhone(addTenantUserDTO.getUserPhone());
            if (userId==null||userId.equals("")){
                userId= DateUtil.timeStamp()+ Tools.random(10,99);
                Integer sex=addTenantUserDTO.getSex().equals("男")?1:2;
                this.roomCustomerMapper.addUser(userId,sex+"",addTenantUserDTO.getUserPhone(),addTenantUserDTO.getUserName(),addTenantUserDTO.getIdNumber());
            }else {
                Integer sex=addTenantUserDTO.getSex().equals("男")?1:2;
                this.roomCustomerMapper.updateUser(userId,sex+"",addTenantUserDTO.getUserPhone(),addTenantUserDTO.getUserName(),addTenantUserDTO.getIdNumber());
            }
            userId=this.roomCustomerMapper.userIsNullFindByPhone(addTenantUserDTO.getUserPhone());
            this.roomCustomerMapper.addTenantUser(userId,addTenantUserDTO.getRoomCodes());
            jsonEntityUtil =new JsonEntityUtil("1000","新增成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getTenantUser(String userId, String userIdAppKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String mine=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            List roomCodes=this.roomCustomerMapper.getTenantRoom(userId,mine);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomCodes);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil updateTenant(UpdateTenantDTO updateTenantDTO) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            Integer userId=this.roomCustomerMapper.updateUser(updateTenantDTO.getUserId(),
                    updateTenantDTO.getSex().equals("男")?"1":"2",
                    updateTenantDTO.getUserPhone()
                    ,updateTenantDTO.getUserName().substring(0,1),updateTenantDTO.getIdNumber());
            for (AddTenantUserRoomDTO s:updateTenantDTO.getRoomCodes()){
               String roomCode=this.roomCustomerMapper.tenantRoomIsNull(s.getRoomCode(),updateTenantDTO.getUserId());
               if (roomCode==null||roomCode.equals("")){
                    this.roomCustomerMapper.addTenantRoom(updateTenantDTO.getUserId(),s);
               }else {
                   this.roomCustomerMapper.updateTenantRoom(updateTenantDTO.getUserId(),s);
               }
            }
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getPorperty(String roomCode) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            List list=this.roomCustomerMapper.getPerporty(roomCode);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil proprietorGetVerificationCode(String appKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(appKey);
            String phone=this.roomCustomerMapper.getPhoneByUserId(userId);
            jsonEntityUtil=SendSMSUtil.sendMsg(phone,this.redisUtil);
            return jsonEntityUtil;
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1003","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getTenantuserRoomCode(String tenantuserId,String appKey) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(appKey);
            List roomCodes=this.roomCustomerMapper.getRoomCodesIdentity(userId, "1");
            List roomInfo=this.roomCustomerMapper.getRoomCodesTenantuser(tenantuserId,roomCodes);
            jsonEntityUtil =new JsonEntityUtil("1000","成功");
            jsonEntityUtil.setEntity(roomInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil =new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }
}
