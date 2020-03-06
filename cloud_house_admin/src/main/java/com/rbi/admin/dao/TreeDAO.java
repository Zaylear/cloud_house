package com.rbi.admin.dao;

import com.rbi.admin.entity.dto.Organization2DTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TreeDAO {
    @Select("select * from organization")
    List<Organization2DTO> findTree();
}
