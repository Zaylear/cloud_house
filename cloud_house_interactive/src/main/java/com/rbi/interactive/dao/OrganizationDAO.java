package com.rbi.interactive.dao;

import com.rbi.interactive.entity.OrganizationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrganizationDAO extends JpaRepository<OrganizationDO, Integer>, JpaSpecificationExecutor<OrganizationDO> {

    OrganizationDO findByOrganizationId(String organizationId);

}
