package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ChargeItemDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChargeItemDAO extends JpaRepository<ChargeItemDO,Integer>, JpaSpecificationExecutor<ChargeItemDO> {

    ChargeItemDO findByChargeCodeAndOrganizationId(String chargeCode,String organizationId);

    List<ChargeItemDO> findByOrganizationIdAndEnableAndChargeTypeIn(String organizationId,int enable,List<Integer> chargeStatus);

    List<ChargeItemDO> findByOrganizationIdAndRefund(String organizationId,int refund);

    List<ChargeItemDO> findByOrganizationId(String organizationId);

    ChargeItemDO findByOrganizationIdAndChargeTypeAndEnable(String organizationId,int chargeType,int enable);

    List<ChargeItemDO> findAllByOrganizationIdAndEnableAndMustPay(String organizationId,int enable, int mustPay);

}
