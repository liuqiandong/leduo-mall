package com.leduo.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.leduo.mall.dao")
public class LeduoMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeduoMallApplication.class, args);
    }

}
