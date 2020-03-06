package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.DistrictDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictDAO extends JpaRepository<DistrictDO,Integer>, JpaSpecificationExecutor<DistrictDO> {
    @Query(value = "select * from regional_district Limit ?1,?2",nativeQuery = true)
    public List<DistrictDO> findByPage(@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from regional_district",nativeQuery = true)
    public int findNum();
}
