package com.rbi.wx.wechatpay.service;

import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.entity.UserBindingEntity;
import org.springframework.web.multipart.MultipartFile;


public interface UserInfoService {
    JsonEntityUtil findByOpenId(String openId);
    JsonEntityUtil findOpenIdCache(String openId);
    JsonEntityUtil userBinding(UserBindingEntity userBindingEntity,String openId);
    JsonEntityUtil getIndexUserInfo(String userId);
    JsonEntityUtil uploadPhoto(String userId, MultipartFile photo);
    JsonEntityUtil updateUserName(String userId,String userName);
    JsonEntityUtil updatePassword(String newPwd,String oldPsw,String userId);
    JsonEntityUtil getuser(String userId);
    JsonEntityUtil userBindingGetVerificationCode(String phone,String surname);
}
