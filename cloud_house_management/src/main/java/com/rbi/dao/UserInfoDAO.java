package com.rbi.dao;

import com.rbi.entity.UserInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserInfoDAO extends JpaRepository<UserInfoDO, Integer>, JpaSpecificationExecutor<UserInfoDO> {

    UserInfoDO findByUserId(String userId);

    UserInfoDO findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sys_user_info SET login_status = ?1 WHERE user_id = ?2",nativeQuery = true)
    void updateLoginStatus(Integer loginStatus, String userId);

    List<UserInfoDO> findAllByOrganizationId(String organizationId);

    List<UserInfoDO> findAllByLoginStatus(Integer loginStatus);
}
