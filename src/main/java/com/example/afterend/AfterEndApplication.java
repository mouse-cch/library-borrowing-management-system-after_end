package com.example.afterend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园图书借阅管理系统 - 启动类
 * @author cch
 * @since 2026-06-29
 */
@SpringBootApplication
@MapperScan("com.example.afterend.mapper")
public class AfterEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(AfterEndApplication.class, args);
    }
}
