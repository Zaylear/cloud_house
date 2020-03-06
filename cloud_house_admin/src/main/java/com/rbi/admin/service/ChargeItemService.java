package com.rbi.admin.service;

import com.rbi.admin.dao.ChargeItemDAO;
import com.rbi.admin.entity.edo.ChargeItemDO;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeItemService {

    @Autowired
    ChargeItemDAO chargeItemDAO;

    public List<ChargeItemDO> findByOrganizationIdAndEnable(String organizationId, int enable) {
        List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findByOrganizationIdAndEnable(organizationId,enable);
        return chargeItemDOS;
    }


    public PageData findByPage(String organizationId,int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<ChargeItemDO> chargeItemDOS  = chargeItemDAO.findByPage(organizationId,pageNo,pageSize);
        int totalPage = 0;
        int count = chargeItemDAO.findNum(organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,chargeItemDOS);
    }


}
