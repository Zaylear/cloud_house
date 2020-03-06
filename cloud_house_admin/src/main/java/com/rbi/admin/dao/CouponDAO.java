package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.CouponDO;
import com.rbi.admin.entity.dto.CouponDO2;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponDAO extends JpaRepository<CouponDO,Integer>, JpaSpecificationExecutor<CouponDO> {

    @Query(value = "select * from pay_coupon where organization_id = ?1 Limit ?2,?3",nativeQuery = true)
    public List<CouponDO2> findByPage(@Param("organizationId")String organizationId, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from pay_coupon where organization_id = ?",nativeQuery = true)
    public int findNum(@Param("organizationId") String organizationId);



}
