package com.rbi.admin.dao;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IdsDeleteDAO {
    @Delete("delete from pay_coupon where id in ${ids}")
    void deleteCouponByIds(@Param("ids") String ids);

    @Delete("delete from organization where id in ${ids}")
    void deleteOrganizationByIds(@Param("ids") String ids);

    @Delete("delete from department where id in ${ids}")
    void deleteDepartmentByIds(@Param("ids") String ids);

    @Delete("delete from charge_item where charge_code in ${chargeCodes}")
    void deleteChargeItem(@Param("chargeCodes") String chargeCodes);

    @Delete("delete from charge_detail where charge_code in ${chargeCodes}")
    void deleteChargeDetail(@Param("chargeCodes") String chargeCodes);




    //省市区多id删除
    @Delete("delete from regional_province where id in ${ids}")
    void deleteProvince(@Param("ids") String ids);

    @Delete("delete from regional_city where id in ${ids}")
    void deleteCity(@Param("ids") String ids);

    @Delete("delete from regional_district where id in ${ids}")
    void deleteDistrict(@Param("ids") String ids);

    @Delete("delete from regional_village where id in ${ids}")
    void deleteVillage(@Param("ids") String ids);

    @Delete("delete from sys_user_info where id in ${ids}")
    void deleteUserByIds(@Param("ids") String ids);

    @Delete("delete from regional_divison where id in ${ids}")
    void deleteDivisonByIds(@Param("ids") String ids);


}
