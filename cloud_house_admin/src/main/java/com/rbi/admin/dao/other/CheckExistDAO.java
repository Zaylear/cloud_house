package com.rbi.admin.dao.other;

import com.rbi.admin.entity.dto.CheckExist.UsernameDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CheckExistDAO {
    @Select("select username from sys_user_info where username = #{username}")
    List<UsernameDTO>findUsername(@Param("username")String username);
}
