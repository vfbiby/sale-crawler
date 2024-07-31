package com.muhuang.salecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SaleCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleCrawlerApplication.class, args);
    }

}
