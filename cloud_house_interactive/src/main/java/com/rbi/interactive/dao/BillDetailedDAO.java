package com.rbi.interactive.dao;

import com.rbi.interactive.entity.BillDetailedDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BillDetailedDAO extends JpaRepository<BillDetailedDO,Integer>, JpaSpecificationExecutor<BillDetailedDAO> {

    List<BillDetailedDO> findAllByOrderIdAndSplitStateAndOrganizationIdAndStateOfArrears(String orderId,int splitState,String organizationId,int stateOfArrears);

    BillDetailedDO findByBillDetailedId(int billDetailedId);

    List<BillDetailedDO> findAllByOrderIdAndOrganizationId(String orderId,String organizationId);
}
