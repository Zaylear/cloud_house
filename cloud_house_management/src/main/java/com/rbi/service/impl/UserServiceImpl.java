package com.rbi.service.impl;

import com.rbi.dao.UserInfoDAO;
import com.rbi.entity.UserInfoDO;
import com.rbi.service.UserService;
import com.rbi.util.Md5Util;
import com.rbi.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Override
    public UserInfoDO findByUserId(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        return userInfoDO;
    }

    @Override
    public Boolean updatePassword(UserInfoDO userInfoDO,String newPassword) throws Exception {
        /**
         * 做原始密码验证
         */
        UserInfoDO userInfoDO1 = userInfoDAO.findByUserId(userInfoDO.getUserId());
        if (Md5Util.getMD5(userInfoDO.getPassword(),userInfoDO.getSalt()).equals(userInfoDO1.getPassword())){
            String salt = Tools.uuid();
            String password = Md5Util.getMD5(newPassword,salt);
            userInfoDO.setSalt(salt);
            userInfoDO.setPassword(password);
            /**
             * 验证用户名是否已存在
             */
            if (userInfoDO.getUsername().equals(userInfoDO1.getUsername())){
                userInfoDAO.saveAndFlush(userInfoDO);
                return true;
            }else {
                UserInfoDO userInfoDOS = userInfoDAO.findByUsername(userInfoDO.getUsername());
                if (null!=userInfoDOS){
                    return false;
                }else {
                    userInfoDAO.saveAndFlush(userInfoDO);
                    return true;
                }
            }
//            userInfoDAO.saveAndFlush(userInfoDO);
//            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean update(UserInfoDO userInfoDO) {
        UserInfoDO userInfoDO1 = userInfoDAO.findByUserId(userInfoDO.getUserId());
        /**
         * 验证用户名是否已存在
         */
        if (userInfoDO.getUsername().equals(userInfoDO1.getUsername())){
            userInfoDAO.saveAndFlush(userInfoDO);
            return true;
        }else {
            UserInfoDO userInfoDOS = userInfoDAO.findByUsername(userInfoDO.getUsername());
            if (null!=userInfoDOS){
                return false;
            }else {
                userInfoDAO.saveAndFlush(userInfoDO);
                return true;
            }
        }



    }
}
