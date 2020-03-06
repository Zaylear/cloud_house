package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.ChargeItemDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IChargeItemDAO extends JpaRepository<ChargeItemDO,Integer>, JpaSpecificationExecutor<ChargeItemDO> {

    ChargeItemDO findByChargeCode(String chargeCode);

    List<ChargeItemDO> findByOrganizationIdAndAndEnableAndChargeTypeIn(String organizationId, int enable, List<Integer> chargeStatus);

    List<ChargeItemDO> findByOrganizationIdAndRefund(String organizationId, int refund);

    List<ChargeItemDO> findByOrganizationId(String organizationId);

    ChargeItemDO findByOrganizationIdAndChargeTypeAndEnable(String organizationId, int chargeType, int enable);

    List<ChargeItemDO> findAllByOrganizationIdAndEnableAndMustPay(String organizationId, int enable, int mustPay);
}
