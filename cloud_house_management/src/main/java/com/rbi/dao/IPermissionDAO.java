package com.rbi.dao;

import com.rbi.entity.PermitDO;
import com.rbi.entity.dto.PermitDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IPermissionDAO {
    @Select("SELECT permis_code as permisCode,title,color,router,parent_code as parentCode,permis_order as permisOrder " +
            "FROM sys_permit WHERE permis_order = 1 AND parent_code = #{system} and permis_code IN " +
            "(SELECT permis_code FROM sys_role_permit WHERE role_code IN " +
            "(SELECT role_code FROM sys_user_role WHERE user_id = #{userId}))")
    public List<PermitDTO> findPermit(@Param("system") String system,@Param("userId") String userId);

    @Select("SELECT permis_code as permisCode,title,color,router,parent_code as parentCode,permis_order as permisOrder " +
            "FROM sys_permit WHERE permis_order = 0 AND permis_code = #{system} and permis_code IN " +
            "(SELECT permis_code FROM sys_role_permit WHERE role_code IN (SELECT role_code " +
            "FROM sys_user_role WHERE user_id = #{userId}))")
    public List<PermitDTO> findAdminPermit(@Param("system") String system,@Param("userId") String userId);

    @Select("SELECT permis_code as permisCode,title,color,router,parent_code as parentCode,permis_order as permisOrder " +
            "FROM sys_permit WHERE parent_code = #{parentCode} and permis_code IN " +
            "(SELECT permis_code FROM sys_role_permit WHERE role_code IN " +
            "(SELECT role_code FROM sys_user_role WHERE user_id = #{userId}))")
    public List<PermitDTO> findByParentCode(@Param("parentCode") String parentCode,@Param("userId") String userId);

    @Select("select id,permis_code as permisCode,title,menu_permis_flag as menuPermisFlag," +
            "permis_order as permisOrder,router,color,parent_code as parentCode,remark,idt,udt " +
            "from sys_permit limit ${pageNo},${pageSize}")
    public List<PermitDO> findPage(Integer pageNo,Integer pageSize);
}
