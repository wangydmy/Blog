package com.wang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wang.mapper")
public class AdminBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminBlogApplication.class,args);
    }
}
