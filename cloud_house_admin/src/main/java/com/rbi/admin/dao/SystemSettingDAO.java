package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.SystemSettingDO;
import com.rbi.admin.entity.dto.structure.OrganizationStructureDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemSettingDAO extends JpaRepository<SystemSettingDO,Integer>, JpaSpecificationExecutor<SystemSettingDO> {

    @Query(value = "select * from system_setting where organization_id =?1 AND setting_type !='0' ORDER BY setting_type limit ?2,?3",nativeQuery = true)
    List<SystemSettingDO> findByPage(@Param("organizationId")String organizationId,@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);

    @Query(value = "select count(*) from system_setting where organization_id = ?",nativeQuery = true)
    int findNum(String organizationId);

    @Query(value = "select setting_name from system_setting where setting_code = ?",nativeQuery = true)
    String findSettingName(@Param("settingCode") String settingCode);

    @Query(value = "select organization_id from sys_user_info where user_id = ?",nativeQuery = true)
    String findOrganizationId(@Param("userId") String userId);

    @Query(value = "select organization_id from sys_user_info where user_id = ?",nativeQuery = true)
    OrganizationStructureDTO findOrganization(@Param("userId") String userId);

    SystemSettingDO findById(int id);


    //管理端系统设置
    @Query(value = "select * from system_setting ORDER BY setting_type,organization_id limit ?1,?2",nativeQuery = true)
    List<SystemSettingDO> findByPageA(@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);

    @Query(value = "select count(*) from system_setting",nativeQuery = true)
    int findNumA();



}
