package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.dto.User2DTO;
import com.rbi.admin.entity.edo.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface UserDAO{

    @Select("SELECT o.organization_id as organizationId,o.organization_name as organizationName,\n" +
            "department_id AS departmentId,\n" +
            "u.id,user_id AS userId,username,`password`,salt,real_name AS realName,sex,birthday,\n" +
            "u.email,u.address,u.mobile_phone AS mobilePhone,u.enabled,u.identity,\n" +
            "u.login_status AS loginStatus,u.idt,u.udt,u.department_id AS departmentId,\n" +
            "u.portrait_path AS portraitPath\n" +
            "FROM organization AS o,sys_user_info AS u\n" +
            "WHERE o.organization_id = u.organization_id \n" +
            "AND user_id = #{userId}")
    User2DTO findByUserId(String userId);

    @Select("select * from sys_user_info where organization_id = #{organizationId} AND real_name LIKE ${realName} Limit #{pageNo},#{pageSize}")
    List<UserInfoDO> findByName(@Param("organizationId")String organizationId, @Param("realName")String realName,
                                      @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Select("select count(*) from sys_user_info where organization_id = #{organizationId} AND real_name LIKE ${realName}")
    int findByNameNum(@Param("organizationId")String organizationId,@Param("realName")String realName);

    @Update("update sys_user_info set theme = #{theme} where user_id = #{userId}")
    void updateTheme(@Param("theme")String theme,@Param("userId")String userId);


    @Update("update sys_user_info set password = #{password} where user_id = #{userId}")
    void updatePassword(@Param("password")String password,@Param("userId")String userId);








}
