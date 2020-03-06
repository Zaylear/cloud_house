package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.dto.SystemSettingDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface SystemSetDAO {
    @Select("select setting_code,setting_name from system_setting where setting_type = '0'")
    List<SystemSettingDTO> findSettingType();

    @Delete("delete from system_setting where id in ${ids}")
    void deleteByIds(@Param("ids") String ids);

    @Select("select setting_code,setting_name from system_setting where setting_type = #{settingType} and organization_id = #{organizationId}")
    List<SystemSettingDTO> findChoose(@Param("settingType") String settingType,@Param("organizationId") String organizationId);

    @Select("select setting_code,setting_name from system_setting where setting_type = #{settingType} and organization_id = 'RBI'")
    List<SystemSettingDTO> findChoose2(@Param("settingType") String settingType);

    @Select("select setting_code,setting_name from system_setting where setting_type = #{settingType} and organization_id = 'RBI' AND status != 100")
    List<SystemSettingDTO> findPaymentMethod(@Param("settingType") String settingType);

    @Select("select setting_name from system_setting where setting_type = #{settingType} and setting_code = #{settingCode}")
    String findNameBySettingCodeAndName(@Param("settingCode") String settingCode,@Param("settingType") String settingType);

}


