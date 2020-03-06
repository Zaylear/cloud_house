package com.rbi.wx.wechatpay.configuration;

import com.rbi.wx.wechatpay.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class StaticFile implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("webmvc拦截器");
        registry.addInterceptor(new TestInter()).addPathPatterns("/**")
                .excludePathPatterns("/wx/userbinding")
                .excludePathPatterns("/wx/login")
        .excludePathPatterns("/CloudPropertyView/**")
        .excludePathPatterns("/gettoken")
        .excludePathPatterns("/").excludePathPatterns("/error")
                .excludePathPatterns("**.js")
                .excludePathPatterns("/photo/**")
        .excludePathPatterns("/wx/sendmsg");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**.html").addResourceLocations("classpath:static/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:static/js/");
      registry.addResourceHandler("/CloudPropertyView/**").addResourceLocations("classpath:templates/CloudPropertyView/");
        registry.addResourceHandler("/MP_verify_xjCOzr7Sj7dIjcd1.txt").addResourceLocations("classpath:static/MP_verify_xjCOzr7Sj7dIjcd1.txt");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
//                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("photo/**").addResourceLocations(
                "file:"+Constants.PHOTOUPLOADURL+"/");

    }

  @Autowired  private ApplicationContext applicationContext;
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("classpath:/templates");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }


}
