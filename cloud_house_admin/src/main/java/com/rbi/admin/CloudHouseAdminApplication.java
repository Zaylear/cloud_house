package com.rbi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.rbi.admin.entity")
public class CloudHouseAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHouseAdminApplication.class, args);
    }

}
