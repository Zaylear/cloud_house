package com.rbi.controller.task;

import com.rbi.service.interactive.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AutomaticLogOff {

    private final static Logger logger = LoggerFactory.getLogger(AutomaticLogOff.class);

    @Autowired
    LoginService loginService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void automaticLogOff(){
        loginService.automaticLogOff();
    }
}
