package com.rbi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.rbi.entity")
public class CloudHouseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHouseManagementApplication.class, args);
    }

}
