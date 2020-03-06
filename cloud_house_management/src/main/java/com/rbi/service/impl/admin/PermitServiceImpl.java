package com.rbi.service.impl.admin;

import com.rbi.dao.IPermissionDAO;
import com.rbi.dao.PermitDAO;
import com.rbi.entity.PermitDO;
import com.rbi.service.admin.PermitService;
import com.rbi.util.DateUtil;
import com.rbi.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermitServiceImpl implements PermitService {

    @Autowired
    PermitDAO permitDAO;

    @Autowired(required = false)
    IPermissionDAO iPermissionDAO;

    @Override
    public Boolean addPermit(PermitDO permitDO) {

        if (0>=permitDAO.findByPermisCode(permitDO.getPermisCode()).size()){
            String nowTime = DateUtil.date(DateUtil.FORMAT_PATTERN);
            permitDO.setIdt(nowTime);
            permitDO.setUdt(nowTime);
            permitDAO.save(permitDO);
            return true;
        }
        return false;
    }

    @Override
    public void updatePermit(PermitDO permitDO) {
        permitDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        permitDAO.update(permitDO.getTitle(),permitDO.getRouter(),
                permitDO.getColor(),permitDO.getPermisOrder(),
                permitDO.getMenuPermisFlag(),permitDO.getUdt(),permitDO.getPermisCode());
    }

    @Override
    public PageData findPermitByPage(Integer pageNum,Integer pageSize) {
        Integer pageNo = pageSize*(pageNum-1);
        List<PermitDO> permitDOS = iPermissionDAO.findPage(pageNo,pageSize);
        Integer totalPage = 0;
        Integer count = permitDAO.findCount();
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,permitDOS);
    }

    @Override
    public void deletePermitByPermitCodes(List<String> permitCodes) {
        permitDAO.deleteByPermisCodeIn(permitCodes);
    }
}
