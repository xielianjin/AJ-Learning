package com.aj.learning;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

@EnableAsync
@Controller
@SpringBootApplication
public class SpringbootThymeleafApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootThymeleafApplication.class, args);
    }
}
