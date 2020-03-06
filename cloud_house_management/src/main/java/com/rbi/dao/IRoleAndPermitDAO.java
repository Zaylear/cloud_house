package com.rbi.dao;

import com.rbi.entity.dto.RoleAndPermitDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoleAndPermitDAO {
    @Select("SELECT srp.id,sr.role_code AS roleCode,sr.role_name AS roleName,sp.permis_code AS permisCode," +
            "sp.title AS title,sp.remark AS remark FROM sys_role AS sr INNER JOIN sys_role_permit AS srp " +
            "ON sr.role_code = srp.role_code INNER JOIN sys_permit AS sp ON srp.permis_code = sp.permis_code " +
            "WHERE sr.organization_id = #{organizationId} ORDER BY srp.role_code LIMIT ${pageNo},${pageSize}")
    public List<RoleAndPermitDTO> findRoleAndPermitPage(@Param("organizationId") String organizationId,
                                                        @Param("pageNo") Integer pageNo,
                                                        @Param("pageSize") Integer pageSize);

    @Select("SELECT count(*) FROM sys_role AS sr INNER JOIN sys_role_permit AS srp " +
            "ON sr.role_code = srp.role_code INNER JOIN sys_permit AS sp ON srp.permis_code = sp.permis_code " +
            "WHERE sr.organization_id = #{organizationId}")
    public Integer findRoleAndPermitCount(@Param("organizationId") String organizationId);


    @Select("SELECT srp.id,sr.role_code AS roleCode,sr.role_name AS roleName,sp.permis_code AS permisCode," +
            "sp.title AS title,sp.remark AS remark FROM sys_role AS sr INNER JOIN sys_role_permit AS srp " +
            "ON sr.role_code = srp.role_code INNER JOIN sys_permit AS sp ON srp.permis_code = sp.permis_code " +
            "WHERE srp.role_code = #{roleCode} GROUP BY sp.permis_code")
    public List<RoleAndPermitDTO> findRoleAndPermitByRoleCode(@Param("roleCode") String roleCode);
}
