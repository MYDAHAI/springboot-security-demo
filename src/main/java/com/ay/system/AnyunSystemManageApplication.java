package com.ay.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AnyunSystemManageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AnyunSystemManageApplication.class, args);
        System.out.println(11111111);
    }

}
