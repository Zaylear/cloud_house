package com.rbi.interactive.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.rbi.common.interactive.PropertyFeeDueTime;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PropertyFeeDueTimeImpl implements PropertyFeeDueTime {


    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Override
    public String propertyFeeDueTime(String roomCode, String organizationId) {
        String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(roomCode,organizationId);
        return dueTime;
    }
}
