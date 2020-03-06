package com.rbi.interactive.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.rbi.common.interactive.ThisSystemOrderId;
import com.rbi.interactive.service.ThisSystemOrderIdService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ThisSystemOrderIdImpl implements ThisSystemOrderId {

    @Autowired
    ThisSystemOrderIdService thisSystemOrderIdService;

    @Override
    public String thisSystemOrderId(String organizationId) {
        return thisSystemOrderIdService.thisSystemOrderId(organizationId);
    }
}
