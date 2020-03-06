package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.OrganizationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IOrganizationDAO extends JpaRepository<OrganizationDO, Integer>, JpaSpecificationExecutor<OrganizationDO> {

    OrganizationDO findByOrganizationId(String organizationId);

}
