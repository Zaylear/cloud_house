package com.rbi.interactive.service.impl;

import com.rbi.interactive.dao.IOriginalBillDAO;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.OriginalBillDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.service.PrePaymentService;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrePaymentServiceImpl implements PrePaymentService {
    @Autowired(required = false)
    IOriginalBillDAO iOriginalBillDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Override
    public PageData<OriginalBillDO> findPrePaymentByPageAndUserIdDesc(int pageNum,int pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;

        String dueTime = DateUtil.date(DateUtil.YEAR_MONTH_DAY);
        List<OriginalBillDO> originalBillDOS = iOriginalBillDAO.findPrePaymentByPageAndUserIdDesc(dueTime,organizationId,pageNo,pageSize);
        int count = iOriginalBillDAO.findPrePaymentByPageCount(dueTime,organizationId);
        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,originalBillDOS);
    }
}
