package com.rbi.service;

import com.rbi.entity.UserInfoDO;

public interface UserService {


    UserInfoDO findByUserId(String userId);

    Boolean updatePassword(UserInfoDO userInfoDO,String newPassword) throws Exception;

    Boolean update(UserInfoDO userInfoDO);
}
