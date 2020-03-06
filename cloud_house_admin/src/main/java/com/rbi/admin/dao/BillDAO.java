package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.BillDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillDAO extends JpaRepository<BillDO,Integer>, JpaSpecificationExecutor<BillDO> {

    BillDO findByOrderIdAndOrganizationId(String orderId, String organizationId);
}
