package com.rbi.dao;

import com.rbi.entity.PermitDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermitDAO extends JpaRepository<PermitDO,Integer>, JpaSpecificationExecutor<PermitDO> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE sys_permit SET title = ?1,router = ?2,color=?3," +
            "permis_order=?4,menu_permis_flag=?5,udt=?6 " +
            "WHERE permis_code = ?7",nativeQuery = true)
    public void update(String title,String router,String color,Integer permisOrder,Integer menuPermisFlag,String udt,String permisCode);

    @Transactional
    void deleteByPermisCodeIn(List<String> permitCodes);

    @Transactional
    @Query(value = "select count(*) from sys_permit",nativeQuery = true)
    public Integer findCount();

    List<PermitDO> findByMenuPermisFlagNotOrderByParentCode(Integer menuPermisFlag);

    List<PermitDO> findAll();

    List<PermitDO> findByPermisCode(String permisCode);

    List<PermitDO> findAllByPermisCodeIn(List<String> permitDOS);
}
