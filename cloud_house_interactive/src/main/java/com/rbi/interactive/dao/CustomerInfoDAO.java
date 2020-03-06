package com.rbi.interactive.dao;

import com.rbi.interactive.entity.CustomerInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerInfoDAO extends JpaRepository<CustomerInfoDO,Integer>, JpaSpecificationExecutor<CustomerInfoDO> {

    List<CustomerInfoDO> findByMobilePhone(String mobilePhone);

    CustomerInfoDO findAllByUserId(String userId);

    CustomerInfoDO findByIdNumber(@Param("idNumber") String idNumber);



}
