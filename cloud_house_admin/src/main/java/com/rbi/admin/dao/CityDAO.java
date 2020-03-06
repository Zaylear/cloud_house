package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.CityDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityDAO extends JpaRepository<CityDO,Integer>, JpaSpecificationExecutor<CityDO> {
    @Query(value = "select * from regional_city Limit ?1,?2",nativeQuery = true)
    public List<CityDO> findByPage(@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from regional_city",nativeQuery = true)
    public int findNum();
}
