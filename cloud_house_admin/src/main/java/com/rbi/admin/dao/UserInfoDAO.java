package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface UserInfoDAO extends JpaRepository<UserInfoDO,Integer>, JpaSpecificationExecutor<UserInfoDO> {
    //验证使用
    UserInfoDO findByUserId(String userId);

    //修改使用
    UserInfoDO findById(int id);

    @Query(value = "select * from sys_user_info where organization_id = ?1 limit ?2,?3",nativeQuery = true)
    List<UserInfoDO> findByPage(@Param("organizationId")String organizationId,@Param("page")int page,@Param("row")int row);

    @Query(value = "select count(*) from sys_user_info where organization_id = ?",nativeQuery = true)
    int findNum(@Param("organizationId")String organizationId);




}
