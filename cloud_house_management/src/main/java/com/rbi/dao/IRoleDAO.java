package com.rbi.dao;

import com.rbi.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoleDAO {

    @Select("SELECT id,idt,remark,role_code,role_name,udt,organization_id FROM sys_role " +
            "WHERE organization_id = #{organizationId} limit #{pageNo},#{pageSize}")
    public List<RoleDO> findByOrganizationIdAndPage(
            @Param("organizationId") String organizationId,
            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) FROM sys_role WHERE organization_id = #{organizationId}")
    public int findByOrganizationIdCount(@Param("organizationId") String organizationId);

    @Select("SELECT id,idt,remark,role_code,role_name,udt,organization_id FROM sys_role " +
            "limit #{pageNo},#{pageSize}")
    public List<RoleDO> findByPage(@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) FROM sys_role")
    public int findByCount();

    @Select("SELECT id,idt,remark,role_code,role_name,udt,organization_id FROM sys_role " +
            "limit #{pageNo},#{pageSize}")
    public List<RoleDO> findAllPage(
            @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    @Select("SELECT count(*) FROM sys_role")
    public int findAllCount();

}
