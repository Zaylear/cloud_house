package com.rbi.interactive.dao;

import com.rbi.interactive.entity.ReportDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReportDAO extends JpaRepository<ReportDO,Integer>, JpaSpecificationExecutor<ReportDO> {

    List<ReportDO> findByParentCode(String parentCode);

    List<ReportDO> findByParentCodeAndParamType(String parentCode,Integer paramType);
}
