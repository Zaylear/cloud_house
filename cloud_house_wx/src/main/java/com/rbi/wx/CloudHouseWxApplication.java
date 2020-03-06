package com.rbi.wx;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@SpringBootApplication
@MapperScan(basePackages="com.rbi.wx.wechatpay.mapper")
public class CloudHouseWxApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CloudHouseWxApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudHouseWxApplication.class, args);
    }

}
