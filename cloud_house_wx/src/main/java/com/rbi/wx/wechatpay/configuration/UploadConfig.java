package com.rbi.wx.wechatpay.configuration;

import com.rbi.wx.wechatpay.util.Constants;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableAutoConfiguration
public class UploadConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/var/www/html/wx-header");
        return factory.createMultipartConfig();
    }
}
