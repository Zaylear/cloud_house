package com.rbi.service.admin;

import com.rbi.entity.PermitDO;
import com.rbi.util.PageData;

import java.util.List;


public interface PermitService {

    public Boolean addPermit(PermitDO permitDO);

    public void updatePermit(PermitDO permitDO);

    public PageData findPermitByPage(Integer pageNo,Integer pageSize);

    public void deletePermitByPermitCodes(List<String> permitCodes);

}
