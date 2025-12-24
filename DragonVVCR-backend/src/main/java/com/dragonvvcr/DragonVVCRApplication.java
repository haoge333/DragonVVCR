package com.dragonvvcr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dragonvvcr.mapper")
public class DragonVVCRApplication {

    public static void main(String[] args) {
        SpringApplication.run(DragonVVCRApplication.class, args);
    }

}
