package com.skitrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SkiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkiBackendApplication.class, args);
    }

}
