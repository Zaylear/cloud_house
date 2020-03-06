package com.rbi.chbd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.rbi.chbd.entity")
public class CloudHouseChbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHouseChbdApplication.class, args);
    }

}
