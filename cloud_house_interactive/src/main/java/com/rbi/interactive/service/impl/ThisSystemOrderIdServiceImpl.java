package com.rbi.interactive.service.impl;

import com.rbi.interactive.dao.IBillDAO;
import com.rbi.interactive.service.ThisSystemOrderIdService;
import com.rbi.interactive.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThisSystemOrderIdServiceImpl implements ThisSystemOrderIdService {

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Override
    public String thisSystemOrderId(String organizationId) {
        String orderId = "";
        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
        orderIdsCount = orderIdsCount+1;
        orderId = time+String.format("%08d", orderIdsCount);
        return orderId;
    }
}
