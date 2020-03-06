package com.rbi.admin.dao;

import com.rbi.admin.entity.dto.Organization2DTO;
import com.rbi.admin.entity.dto.OrganizationDTO;
import com.rbi.admin.entity.edo.OrganizationDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrganizationDAO extends JpaRepository<OrganizationDO,Integer>, JpaSpecificationExecutor<OrganizationDO> {

    @Query(value = "select * from organization Limit ?1,?2",nativeQuery = true)
    List<OrganizationDO> findByPage(@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);

    @Query(value = "select count(id) from organization",nativeQuery = true)
    int findNum();

    OrganizationDO findById(@Param("id")int id);

    @Query(value = "select organization_id,organization_name from organization where pid=0",nativeQuery = true)
    List<OrganizationDTO> findOrganizationId();


    @Query(value = "select * from organization",nativeQuery = true)
    List<Organization2DTO> findTree();

    @Query(value = "select organization_name from organization where organization_id = ?",nativeQuery = true)
    String findNameByOrganizationId(@Param("organizationId")String organizationId);


    @Query(value = "select count(*) from organization where organization_id = ?",nativeQuery = true)
    int findIdNum(@Param("organizationId")String organizationId);

}
