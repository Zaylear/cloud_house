package com.rbi.admin.dao;


import com.rbi.admin.entity.edo.ProvinceDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinceDAO extends JpaRepository<ProvinceDO,Integer>, JpaSpecificationExecutor<ProvinceDO> {
    @Query(value = "select * from regional_province Limit ?1,?2",nativeQuery = true)
    public List<ProvinceDO> findByPage(@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from regional_province",nativeQuery = true)
    public int findNum();
}
