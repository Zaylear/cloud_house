package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.DivisonDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface DivisonDAO extends JpaRepository<DivisonDO,Integer>, JpaSpecificationExecutor<DivisonDO> {
//    @Query(value = "select * from regional_divison where flag != ?1",nativeQuery = true)
//    List<DivisonDO> findTree(@Param("flag") Integer flag);

//    @Query(value = "select * from regional_divison",nativeQuery = true)
//    List<DivisonDO> findTree();

    @Override
    List<DivisonDO> findAll();

    @Query(value = "select * from regional_divison where id = ?",nativeQuery = true)
    DivisonDO findDetail(@Param("id") Integer id);


}
