package com.rbi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IPermissionAuthenticationDao {

    @Select("SELECT permis_url FROM sys_permit WHERE permis_code IN " +
            "(SELECT permis_code FROM sys_role_permit WHERE role_code IN " +
            "(SELECT role_code FROM sys_user_role WHERE user_id = #{userId}))")
    public List<String> findPermisUrlS(String userId);

}
