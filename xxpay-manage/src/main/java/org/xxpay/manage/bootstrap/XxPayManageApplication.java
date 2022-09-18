package org.xxpay.manage.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * 运营平台启动入口
 */
@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages={"org.xxpay"})
@ImportResource(locations = {"classpath:application-datasource.xml"})
public class XxPayManageApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XxPayManageApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.listeners();
        return application.sources(applicationClass);
    }

    private static Class<XxPayManageApplication> applicationClass = XxPayManageApplication.class;

}