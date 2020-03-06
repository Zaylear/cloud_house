package com.rbi.dao;

import com.rbi.entity.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Test {

    @Select("SELECT * FROM sys_user_info")
    @Results({
            @Result(property = "userId",column = "user_id"),
            @Result(property = "loginStatus",column = "login_status")
    })
    public List<UserInfoDO> findAll();

}
