package com.rbi.interactive.dao;

import com.rbi.interactive.entity.CostDeductionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;

public interface CostDeductionDAO extends JpaRepository<CostDeductionDO,Integer>, JpaSpecificationExecutor<CostDeductionDO> {

    ArrayList<CostDeductionDO> findAllByOrderIdAndOrganizationId(String orderId,String organizationId);

}
