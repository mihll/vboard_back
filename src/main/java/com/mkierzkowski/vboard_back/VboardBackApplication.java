package com.mkierzkowski.vboard_back;

import com.mkierzkowski.vboard_back.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class VboardBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(VboardBackApplication.class, args);
    }

}
