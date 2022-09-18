package org.xxpay.task.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务启动入口（对账和结算等）
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages={"org.xxpay"})
@ImportResource(locations = {"classpath:application-datasource.xml"})
public class XxPayTaskApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XxPayTaskApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.listeners();
        return application.sources(applicationClass);
    }

    private static Class<XxPayTaskApplication> applicationClass = XxPayTaskApplication.class;

}