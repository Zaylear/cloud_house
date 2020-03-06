package com.rbi.dao;

import com.rbi.entity.RoleAndPermitDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleAndPermitDAO extends JpaRepository<RoleAndPermitDO,Integer>, JpaSpecificationExecutor<RoleAndPermitDO> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

    @Transactional
    void deleteById(Integer id);


    RoleAndPermitDO findByRoleCodeAndPermisCode(String roleCode,String permisCode);

    List<RoleAndPermitDO> findAllByRoleCodeAndPermisCode(String roleCode,String permisCode);

}
