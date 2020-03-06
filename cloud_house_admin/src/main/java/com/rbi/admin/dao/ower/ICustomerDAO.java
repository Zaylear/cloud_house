package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.CustomerInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ICustomerDAO extends JpaRepository<CustomerInfoDO,Integer>, JpaSpecificationExecutor<CustomerInfoDO> {

    List<CustomerInfoDO> findByMobilePhone(String phone);

    CustomerInfoDO findByIdNumber(String idNumber);

    CustomerInfoDO findByUserId(String userId);

    CustomerInfoDO findByMobilePhoneAndEnabled(String mobilePhone, Integer enabled);
}
