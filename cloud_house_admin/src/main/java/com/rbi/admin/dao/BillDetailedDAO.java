package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.BillDetailedDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface BillDetailedDAO extends JpaRepository<BillDetailedDO,Integer>, JpaSpecificationExecutor<BillDetailedDAO> {

    List<BillDetailedDO> findAllByOrderIdAndSplitStateAndOrganizationId(String orderId, int splitState, String organizationId);

    BillDetailedDO findByBillDetailedId(int billDetailedId);

    List<BillDetailedDO> findAllByOrderIdAndOrganizationId(String orderId, String organizationId);
}
