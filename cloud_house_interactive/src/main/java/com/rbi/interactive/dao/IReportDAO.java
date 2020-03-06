package com.rbi.interactive.dao;

import com.rbi.interactive.entity.OriginalBillDO;
import com.rbi.interactive.entity.ReportDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IReportDAO {

    @Select("${sql}")
    public List<OriginalBillDO> findDataByPage(@Param("sql") String sql);

    @Select("${sqlCount}")
    public int findDataCount(@Param("sqlCount") String sqlCount);

}
