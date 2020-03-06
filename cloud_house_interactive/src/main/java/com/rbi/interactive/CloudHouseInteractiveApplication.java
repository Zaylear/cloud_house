package com.rbi.interactive;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
@MapperScan(value = "com.rbi.interactive.entity")
public class CloudHouseInteractiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHouseInteractiveApplication.class, args);
    }

}
