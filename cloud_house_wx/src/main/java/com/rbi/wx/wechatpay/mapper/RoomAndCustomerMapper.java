package com.rbi.wx.wechatpay.mapper;

import com.rbi.wx.wechatpay.dto.*;
import com.rbi.wx.wechatpay.entity.UserBindingEntity;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.UpadatePasswordRequestEntity;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomAndCustomerMapper {
    @Select("select MIN(identity) from room_customer where user_id=#{userId}")
    Integer findIdentityById(String userId);
    @Select("select room_code from room_customer where user_id=#{userId} and " +
            "past_due = 0 and " +
            "logged_off_state = 0")
    List<String> findRoomCode(String userId);
    @Select("select DISTINCT user_id from wx_user_info where open_id = #{openId} and status = '0'")
    String userIsNull(String openId);
    @Select("select DISTINCT user_id from sys_customer_info where mobile_phone = #{phone} and enabled = '1'")
    String userIsNullByPhone(String phone);

    @Select("SELECT\n" +
            "sys_customer_info.user_id AS userId,\n" +
            "sys_customer_info.username AS userName,\n" +
            "sys_customer_info.mobile_phone AS mobilePhone\n" +
            "FROM\n" +
            "sys_customer_info\n" +
            "where mobile_phone = #{mobilePhone}")
    String userIdIsNull(String mobilePhone);
    @Insert("insert into sys_customer_info " +
            "(user_id,idt,udt,mobile_phone,password,salt,surname) VALUES" +
            "(#{userId},NOW(),NOW(),#{userBindingEntity.mobilePhone},#{userBindingEntity.password},#{salt},#{userBindingEntity.userName})")
    void addUser(@Param("userBindingEntity") UserBindingEntity userBindingEntity,@Param("userId") String userId,@Param("salt")String salt);
    @Update("update sys_customer_info set udt=NOW(),mobile_phone=#{userBindingEntity.mobilePhone}," +
            "password=#{userBindingEntity.password},salt=#{salt}" +
            "where user_id=#{userId}")
    void updateUser(@Param("userBindingEntity") UserBindingEntity userBindingEntity,@Param("userId") String userId,@Param("salt")String salt);
    @Insert("insert into wx_user_info (user_id,open_id,idt,udt,wx_user_name) values" +
            "(#{userId},#{openId},NOW(),NOW(),#{surname})")
    void addUserOpenId(@Param("surname")String surname,@Param("userId") String userId,@Param("openId") String openId);
    @Select("select user_id from wx_user_info where status ='0' and " +
            "open_id=#{OpenId}")
    String getUserId(String OpenId);
    @Select("select enabled from sys_customer_info where user_id=#{userId}")
    String findEnabled(String userId);

    @Select("SELECT\n DISTINCT " +
            "sys_customer_info.user_id AS userId,\n" +
            "wx_user_info.wx_user_name AS userName,\n" +
            "wx_user_info.header_path AS path,\n" +
            "(CASE  WHEN sys_customer_info.sex= '1' THEN '男' ELSE '女' END) AS sex,\n" +
            "sys_customer_info.mobile_phone AS mobilePhone,\n" +
            "(Select MAX(identity) from room_customer where room_customer.customer_user_id = #{userId}) AS maxIdentity "+
            "FROM\n" +
            "wx_user_info\n" +
            "INNER JOIN sys_customer_info ON wx_user_info.user_id = sys_customer_info.user_id\n" +
            "where sys_customer_info.user_id = #{userId}")
    LoginUserInfoDTO findUserInfo(String userId);
    @Update("update wx_user_info set wx_user_name=#{userName},udt=NOW() where status = '0' and user_id=#{userId}")
    Integer updateUserName(@Param("userId")String userId,@Param("userName")String userName);

    @Update("update wx_user_info set header_path=#{photoPath},udt=NOW() where status = '0' and user_id=#{userName}")
    Integer updateUserPhoto(@Param("photoPath")String photoPath,@Param("userName") String userName);
    @Select("select salt,password from sys_customer_info where user_id=#{userId}")
    UpdatePasswordEntity getSaltByUserId(String userId);
    @Update("update sys_customer_info set password=#{password},udt=NOW() where user_Id=#{userId} and enabled = '1'")
    Integer updatePassword(@Param("userId") String userId,@Param("password") String password);
    @Select("select header_path from wx_user_info where user_id=#{userId}")
    String getHeaderPath(String userId);
    @Select("select (CASE  WHEN sys_customer_info.sex= '1' THEN '男' ELSE '女' END) AS sex," +
            "sys_customer_info.surname as userName," +
            "id_number as idNumber," +
            "mobile_phone AS userPhone from sys_customer_info where user_id=#{userId} and" +
            " enabled= '1 '")
    GetUserInfoDTO getuser(String userId);
    @Select("SELECT\n" +
            "sys_customer_info.mobile_phone\n" +
            "FROM\n" +
            "sys_customer_info where surname=#{surname} and mobile_phone=#{phone} and enabled='1'")
    String getPhoneByPhoneAndSurName(@Param("phone")String idCard,@Param("surname")String surname);

    @Select("SELECT\n" +
            "sys_customer_info.user_id\n" +
            "FROM\n" +
            "sys_customer_info where surname=#{surname} and mobile_phone=#{phone} and enabled='1'")
    String getUserIdByPhoneAndSurname(@Param("surname")String surname,@Param("phone")String phone);
    @Select("select user_id from wx_user_info where open_id=#{openId} and status='0'")
    String getUserIdByOpenId(String openId);
}
