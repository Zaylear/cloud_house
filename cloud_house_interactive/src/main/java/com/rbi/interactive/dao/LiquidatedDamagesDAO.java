package com.rbi.interactive.dao;

import com.rbi.interactive.entity.LiquidatedDamagesDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LiquidatedDamagesDAO extends JpaRepository<LiquidatedDamagesDO,Integer>, JpaSpecificationExecutor<LiquidatedDamagesDO> {

    @Override
    List<LiquidatedDamagesDO> findAll();

    LiquidatedDamagesDO findById(int id);

    @Transactional
    void deleteByIdIn(List<Integer> ids);
}
