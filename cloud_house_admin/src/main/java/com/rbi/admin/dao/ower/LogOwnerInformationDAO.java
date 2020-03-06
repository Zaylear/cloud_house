package com.rbi.admin.dao.ower;

import com.rbi.admin.entity.edo.LogOwnerInformationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogOwnerInformationDAO extends JpaRepository<LogOwnerInformationDO,Integer>, JpaSpecificationExecutor<LogOwnerInformationDO> {
}
