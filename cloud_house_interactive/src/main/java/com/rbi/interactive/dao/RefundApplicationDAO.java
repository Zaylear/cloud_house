package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RefundApplicationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefundApplicationDAO extends JpaRepository<RefundApplicationDO,Integer>, JpaSpecificationExecutor<RefundApplicationDO> {

    RefundApplicationDO findById(int id);

    RefundApplicationDO findByOrderId(String orderId);

}
