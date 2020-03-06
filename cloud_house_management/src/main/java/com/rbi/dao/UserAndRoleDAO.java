package com.rbi.dao;

import com.rbi.entity.UserAndRoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserAndRoleDAO extends JpaRepository<UserAndRoleDO,Integer>, JpaSpecificationExecutor<UserAndRoleDO> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

}
