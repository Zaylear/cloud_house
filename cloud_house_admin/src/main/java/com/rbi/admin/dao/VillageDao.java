package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.VillageDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VillageDao extends JpaRepository<VillageDO,Integer>, JpaSpecificationExecutor<VillageDO> {
    @Query(value = "select * from regional_village Limit ?1,?2",nativeQuery = true)
    public List<VillageDO> findByPage(@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    @Query(value = "select count(id) from regional_village",nativeQuery = true)
    public int findNum();

}
