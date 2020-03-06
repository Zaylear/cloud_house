package com.rbi.dao;

import com.rbi.entity.dto.UserAndRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IUserAndRoleDAO {

    @Select("SELECT sur.id,sui.user_id AS userId,sui.username AS username,sui.real_name AS realName," +
            "sr.role_code AS roleCode,sr.role_name AS roleName FROM sys_user_info AS sui INNER JOIN " +
            "sys_user_role AS sur ON sui.user_id = sur.user_id INNER JOIN sys_role AS sr ON " +
            "sur.role_code = sr.role_code WHERE sui.organization_id = #{organizationId} " +
            "ORDER BY sui.user_id limit ${pageNo},${pageSize}")
    public List<UserAndRoleDTO> findUserAndRolePage(String organizationId,Integer pageNo,Integer pageSize);

    @Select("SELECT count(*) FROM sys_user_info AS sui INNER JOIN " +
            "sys_user_role AS sur ON sui.user_id = sur.user_id INNER JOIN sys_role AS sr ON " +
            "sur.role_code = sr.role_code WHERE sui.organization_id = #{organizationId}")
    public Integer findUserAndRoleCount(String organizationId);


    @Select("SELECT sur.id,sui.user_id AS userId,sui.username AS username,sui.real_name AS realName," +
            "sr.role_code AS roleCode,sr.role_name AS roleName FROM sys_user_info AS sui INNER JOIN " +
            "sys_user_role AS sur ON sui.user_id = sur.user_id INNER JOIN sys_role AS sr ON " +
            "sur.role_code = sr.role_code WHERE sui.user_id = #{userId}")
    public List<UserAndRoleDTO> findUserAndRoleByUserId(@Param("userId") String userId);
}
