package com.mkierzkowski.vboard_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VboardBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(VboardBackApplication.class, args);
    }

}
