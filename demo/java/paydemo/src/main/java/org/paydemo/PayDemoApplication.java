package org.paydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class PayDemoApplication extends SpringBootServletInitializer {
	
    public static void main(String[] args) {
    	//启动spring boot
        SpringApplication.run(PayDemoApplication.class, args);
    }

}