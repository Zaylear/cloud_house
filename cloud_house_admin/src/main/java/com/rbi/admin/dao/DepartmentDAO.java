package com.rbi.admin.dao;

import com.rbi.admin.entity.edo.DepartmentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentDAO extends JpaRepository<DepartmentDO,Integer>, JpaSpecificationExecutor<DepartmentDO> {

    DepartmentDO findById(@Param("id") int id);

    @Query(value = "select * from department Limit ?1,?2",nativeQuery = true)
    List<DepartmentDO> findByPage(@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);

    @Query(value = "select count(id) from department",nativeQuery = true)
    int findNum();

    @Query(value = "select * from department where organization_id = ?1",nativeQuery = true)
    List<DepartmentDO> findTree(@Param("organizationId") String organizationId);

    int countByDeptId(@Param("deptId") String deptId);

    @Query(value = "select dept_name from department where dept_id = ?",nativeQuery = true)
    String findNameByOrganizationId(@Param("deptId")String deptId);



}
