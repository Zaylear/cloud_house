package com.rbi.interactive.dao;

import com.rbi.interactive.entity.RoomAndCouponDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomAndCouponDAO extends JpaRepository<RoomAndCouponDO,Integer>, JpaSpecificationExecutor<RoomAndCouponDO> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

    @Transactional
    @Modifying
    @Query(value = "update room_coupon set audit_status=2? where id=1?",nativeQuery = true)
    void updateAuditStatus(int id,int auditStatus);

    RoomAndCouponDO findById(int id);
}
