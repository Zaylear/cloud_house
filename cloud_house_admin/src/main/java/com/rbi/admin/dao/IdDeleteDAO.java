package com.rbi.admin.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IdDeleteDAO {
    @Delete("delete from charge_detail where id = #{id}")
    public void deleteChargeDetail(@Param("id")Integer id);

}
