package com.kenny.poc.atp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class AsyncThreadlocalPropagationPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncThreadlocalPropagationPocApplication.class, args);
    }

}
