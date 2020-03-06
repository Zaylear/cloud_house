package com.rbi.dao;

import com.rbi.entity.LogLoginDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ILogLoginDAO {

    @Select("SELECT * FROM log_login WHERE user_id = #{userId} AND idt = (SELECT MAX(idt) FROM log_login WHERE user_id = #{userId})")
    LogLoginDO findNewestLoginData(@Param("userId") String userId);

    @Update("UPDATE log_login SET udt = #{udt} WHERE user_id = #{userId} AND idt = #{idt}")
    void  updateUdt(@Param("udt") Long udt,@Param("userId") String userId,@Param("idt") String idt);

    @Select("SELECT * FROM (SELECT * FROM log_login ORDER BY id DESC LIMIT 1000000000000) AS t GROUP BY user_id")
    List<LogLoginDO> findAllOrderByIDGroupByUserId();
}
