package com.rbi.dao;

import com.rbi.entity.RoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleDAO extends JpaRepository<RoleDO,Integer>, JpaSpecificationExecutor<RoleDO> {

    List<RoleDO> findAllByOrganizationId(String organizationId);

    @Transactional
    void deleteByIdIn(List<Integer> ids);
}
