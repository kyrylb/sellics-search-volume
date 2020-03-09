package com.sellics.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class SellicsApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SellicsApplication.class, args);
    }
}
