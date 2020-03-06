package com.rbi.interactive.controller.task;

import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.service.UnderpaymentStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SavePropertyDueTime {

    private final static Logger logger = LoggerFactory.getLogger(UnderpaymentStatistics.class);

    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void propertyInarrearsStatistics(){
        propertyFeeDueTimeService.findAllPropertyFeeDueTime();
    }
}
