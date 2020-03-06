package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RefundHistoryDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefundDAO extends JpaRepository<RefundHistoryDO,Integer>, JpaSpecificationExecutor<RefundHistoryDO> {

    RefundHistoryDO findByOrderId(String orderId);

}
