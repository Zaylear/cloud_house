package com.rbi.admin.dao.connect;

import com.rbi.admin.entity.dto.WxUserInfoDTO;
import com.rbi.admin.entity.dto.houseinfo.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WxUserInfoDAO {
    @Select("select user_id from sys_user_info where mobile_phone=#{Phone}")
    public String findUserByPhone(String Phone);
    @Insert("inser into sys_user_info(real_name,mobile_phone,sex,idt,udt) values " +
            "(#{realName},#{mobilePhone},#{sex},now(),now()")
    public void insetiUser(WxUserInfoDTO wxUserInfoDTO);
    @Select("<script>" +
            "select" +
            " user_id,user_name,mobile_phone " +
            "from" +
            " sys_user_info" +
            "where" +
            "user_id in" +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item} "+
            "</foreach>" +
            "</script>")
    public List<UserDTO> findPhoneAndName(List list);
}
