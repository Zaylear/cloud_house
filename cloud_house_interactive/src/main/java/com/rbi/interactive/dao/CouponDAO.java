package com.rbi.interactive.dao;

import com.rbi.interactive.entity.CouponDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CouponDAO extends JpaRepository<CouponDO,Integer>, JpaSpecificationExecutor<CouponDO> {

    CouponDO findByCouponCodeAndOrganizationId(String couponCode,String organizationId);

    List<CouponDO> findByOrganizationId(String organizationId);
}
