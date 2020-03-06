package com.rbi.interactive.dao;

import com.rbi.interactive.entity.BillDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillDAO extends JpaRepository<BillDO,Integer>, JpaSpecificationExecutor<BillDO> {

    BillDO findByOrderIdAndOrganizationId(String orderId,String organizationId);

    BillDO findByIdAndOrganizationId(int id,String organizationId);

    BillDO findByOffsetOriginalDocNoAndOrganizationId(String offsetOriginalDocNo,String organizationId);
}
