package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.CustomerInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerInfoDAO{
    CustomerInfoDO findByOpenId(String openId);

//    @Query(value = "SELECT id,user_id as userId,password,salt,sex,surname,mobile_phone as mobilePhone," +
//            "open_id as openId,portrait_path as portraitPath,enabled,login_status as loginStatus," +
//            "idt,udt FROM sys_customer_info WHERE user_id IN (?)",nativeQuery = true)
//        public List<CustomerInfoDO> findByUserIds(@Param("userIds") String userId);
    @Select("SELECT id,user_id as userId,password,salt,sex,surname,mobile_phone as mobilePhone,\" +\n" +
            "            \"open_id as openId,portrait_path as portraitPath,enabled,login_status as loginStatus,\" +\n" +
            "            \"idt,udt FROM sys_customer_info WHERE user_id IN #{userId}")
    public List<CustomerInfoDO> findByUserIds(String userId);
}
