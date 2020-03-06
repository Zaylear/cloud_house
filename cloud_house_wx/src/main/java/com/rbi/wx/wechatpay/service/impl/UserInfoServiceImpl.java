package com.rbi.wx.wechatpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.GetUserInfoDTO;
import com.rbi.wx.wechatpay.dto.UpdatePasswordEntity;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.LoginUserInfoDTO;
import com.rbi.wx.wechatpay.dto.UserInfo;
import com.rbi.wx.wechatpay.entity.UserBindingEntity;
import com.rbi.wx.wechatpay.mapper.RoomAndCustomerMapper;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.UpadatePasswordRequestEntity;
import com.rbi.wx.wechatpay.service.UserInfoService;
import com.rbi.wx.wechatpay.util.*;
import com.rbi.wx.wechatpay.util.msgutil.SendSMSUtil;
import com.rbi.wx.wechatpay.util.password.RSAGetStringUtil;
import com.rbi.wx.wechatpay.util.uploadutils.HTTPUploadUtil;
import org.omg.CORBA.portable.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    private final static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private HTTPRequest request;
    @Autowired
    private RoomAndCustomerMapper roomAndCustomerMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RSAGetStringUtil rsaGetStringUtil;
    /**
     * 通过openId查找用户信息
     * @param openId
     * @return
     */
    @Override
    public JsonEntityUtil findByOpenId(String openId) {
        JsonEntityUtil jsonEntityUtil=null;
        JSONObject jsonObject=new JSONObject();
        String userId=null;
        try {
            userId=this.roomAndCustomerMapper.userIsNull(openId);
        }catch (Exception e)
        {
            return  jsonEntityUtil=new JsonEntityUtil("1003","服务器错误");
        }
        if (userId==null){
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("code","1001");
            jsonObject1.put("msg","账号还未绑定公众号");
            jsonEntityUtil=new JsonEntityUtil("1000","请求成功");
            jsonEntityUtil.setEntity(jsonObject1);
            return  jsonEntityUtil;
        }
        /**
         * 判断用户是否能登录
         */try {
            String enadled=this.roomAndCustomerMapper.findEnabled(userId);
            if (enadled!=null&&enadled.equals("1")) {
                logger.info("用户{}登录成功 ", userId);
                jsonEntityUtil = new JsonEntityUtil("1000", "允许登录");
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("code","1000");
                jsonObject1.put("APPKEY",this.rsaGetStringUtil.getPublicPassword(userId));
                jsonEntityUtil.setEntity(jsonObject1);
            }else {
                jsonEntityUtil=new JsonEntityUtil("1000","请求成功");
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("code","1002");
                jsonObject1.put("msg","用户信息异常不允许登录");
                jsonEntityUtil.setEntity(jsonObject1);
            }
            }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1003","服务器错误");
        }
        return jsonEntityUtil;
    }

    /**
     * 通过openId在redis里面查找用户信息
     * @param openId
     * @return
     */
    @Override
    public JsonEntityUtil findOpenIdCache(String openId) {
        JsonEntityUtil jsonEntityUtil=null;
        UserInfo userInfo=null;
        try {
            userInfo=(UserInfo) redisUtil.get(openId);
            jsonEntityUtil=new JsonEntityUtil("1000","成功");
            logger.info("用户{}取出数据",userInfo.getUsername());
            jsonEntityUtil.setEntity(userInfo);
        }catch (Exception e){
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    /**
     * 用户绑定业务逻辑
     * @param userBindingEntity
     * @return
     */
    @Override
    public JsonEntityUtil userBinding(UserBindingEntity userBindingEntity,String openId) {
        JsonEntityUtil jsonEntityUtil=null;
        String uuid= UUID.randomUUID().toString();
        try {
          //  String phone=this.roomAndCustomerMapper.getPhoneByIdCardAndSurName(userBindingEntity.getIdCard(),userBindingEntity.getSurname());
            this.redisUtil.setRedisDb(1);
            String verificationCode=this.redisUtil.get("msg"+userBindingEntity.getPhone()).toString();
            if (!userBindingEntity.getVerificationCode().equals(verificationCode)){
                return jsonEntityUtil=new JsonEntityUtil("1002","短信验证码错误");
            }
            String userId=this.roomAndCustomerMapper.getUserIdByPhoneAndSurname(userBindingEntity.getSurName(),userBindingEntity.getPhone());
            String userIdIsNull=this.roomAndCustomerMapper.getUserIdByOpenId(openId);
            if(userIdIsNull==null){
                userIdIsNull="-1";
            }
            if (!userIdIsNull.equals("-1")){
                return jsonEntityUtil=new JsonEntityUtil("1002","请不要重复注册");
            }
            this.roomAndCustomerMapper.addUserOpenId(userBindingEntity.getSurName(),userId,openId);
            jsonEntityUtil=new JsonEntityUtil("1000","允许跳转");
            JSONObject jsonObject=new JSONObject();
            this.redisUtil.setRedisDb(1);
            this.redisUtil.set(uuid,this.rsaGetStringUtil.getPublicPassword(userId),3600);
            jsonObject.put("APPKEY",uuid);
            jsonEntityUtil.setEntity(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;

    }

    @Override
    public JsonEntityUtil getIndexUserInfo(String userIdAppKey) {

        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAppKey);
            LoginUserInfoDTO loginUserInfoDTO=this.roomAndCustomerMapper.findUserInfo(userId);
            jsonEntityUtil=new JsonEntityUtil("1000","允许跳转");
            jsonEntityUtil.setEntity(loginUserInfoDTO);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;

    }

    @Override
    public JsonEntityUtil uploadPhoto(String userIdAPPKEY, MultipartFile photo) {
        System.out.println("开始上传图片"+photo.getName());
        JsonEntityUtil jsonEntityUtil=null;
        String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAPPKEY);
        try {
            if (Objects.isNull(photo)||photo.isEmpty()){
                return jsonEntityUtil=new JsonEntityUtil("1003","图片为空");
            }else {
                String headerPath=this.roomAndCustomerMapper.getHeaderPath(userId);

                byte[] photobyte=photo.getBytes();
                String fileName=photo.getOriginalFilename();
                String photoextension=fileName.substring(fileName.lastIndexOf("."));
                String uploadFileName=DateUtil.date("yyyyMMddHHmmss")+userId+photoextension;
                String uploadFileURL=Constants.PHOTOUPLOADURL+uploadFileName;
                System.out.println("URL"+uploadFileURL);
                /**
                 * 正式上线的时候要修改上传路径
                 * 直接上传至零时文件夹
                 */
//                HTTPUploadUtil httpUploadUtil=new HTTPUploadUtil();
//                httpUploadUtil.postFile(uploadFileURL,photobyte);
                File photoFile=new File(Constants.PHOTOUPLOADURL+"/"+uploadFileName);
                if (headerPath!=null&&!headerPath.equals("")) {
                    String path = headerPath.substring(30);
                    new File(Constants.PHOTOUPLOADURL+"/"+ path).delete();
                }
                photo.transferTo(photoFile);
                jsonEntityUtil=new JsonEntityUtil("1000","上传成功");
                if (this.roomAndCustomerMapper.updateUserPhoto(Constants.PHOTOREADURL+uploadFileName,userId)>0){
                jsonEntityUtil.setEntity(Constants.PHOTOREADURL+uploadFileName);
                }else {
                    jsonEntityUtil.setEntity("头像上传失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil updateUserName(String userIdAPPKEY, String userName) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAPPKEY);

            if (this.roomAndCustomerMapper.updateUserName(userId,userName)>0){
                jsonEntityUtil=new JsonEntityUtil("1000","修改成功");
                jsonEntityUtil.setEntity(userName);
            }else {
                jsonEntityUtil=new JsonEntityUtil("1002","服务器繁忙");
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil updatePassword(String newPwd, String oldPsw, String userIdAPPKEY) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String userId=this.rsaGetStringUtil.getPrivatePassword(userIdAPPKEY);
            UpdatePasswordEntity updatePasswordEntity=this.roomAndCustomerMapper.getSaltByUserId(userId);
            if (MD5Util.MD5Encode((oldPsw+updatePasswordEntity.getSalt()),"UTF-8").equals(updatePasswordEntity.getPassword())){
                String newPassword=MD5Util.MD5Encode(newPwd+updatePasswordEntity.getSalt(),"UTF-8");
                if (this.roomAndCustomerMapper.updatePassword(userId,newPassword)>0){
                    jsonEntityUtil=new JsonEntityUtil("1000","修改密码成功");
                }else {
                jsonEntityUtil=new JsonEntityUtil("1003","修改密码失败");
                }
            }else {
                jsonEntityUtil=new JsonEntityUtil("1003","密码不一致");
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil getuser(String userId) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
                GetUserInfoDTO getUserInfoDTO=this.roomAndCustomerMapper.getuser(userId);
                jsonEntityUtil=new JsonEntityUtil("1000","查询成功");
                jsonEntityUtil.setEntity(getUserInfoDTO);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }

    @Override
    public JsonEntityUtil userBindingGetVerificationCode(String phone, String surname) {
        JsonEntityUtil jsonEntityUtil=null;
        try {
            String phoneIsNull=this.roomAndCustomerMapper.getPhoneByPhoneAndSurName(phone, surname);
            if (phoneIsNull==null||phoneIsNull.equals("")){
                jsonEntityUtil=new JsonEntityUtil("1002","请先到前台或业主处注册信息");
                return jsonEntityUtil;
            }
            jsonEntityUtil= SendSMSUtil.sendMsg(phone,redisUtil);
        }catch (Exception e){
            e.printStackTrace();
            jsonEntityUtil=new JsonEntityUtil("1004","服务器繁忙");
        }
        return jsonEntityUtil;
    }
}
