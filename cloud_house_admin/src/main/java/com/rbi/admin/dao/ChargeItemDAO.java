package com.rbi.admin.dao;


import com.rbi.admin.entity.edo.ChargeItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChargeItemDAO extends JpaRepository<ChargeItemDO,Integer>, JpaSpecificationExecutor<ChargeItemDO> {

    List<ChargeItemDO> findByOrganizationIdAndEnable(String organizationId, int enable);

    @Query(value = "select * from charge_item where organization_id = ?1 Limit ?2,?3",nativeQuery = true)
    List<ChargeItemDO> findByPage(@Param("organizationId")String organizationId,@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from charge_item where organization_id = ?1",nativeQuery = true)
    int findNum(@Param("organizationId")String organizationId);



}
