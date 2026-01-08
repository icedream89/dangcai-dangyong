package com.dangcai.miniapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 小程序API启动类
 *
 * @author dangcai
 * @since 2026-01-07
 */
@SpringBootApplication(scanBasePackages = "com.dangcai")
public class MiniappApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniappApplication.class, args);
        System.out.println("""

                ========================================
                   当才当用 - 小程序API启动成功！
                   访问地址: http://localhost:8081
                   Knife4j文档: http://localhost:8081/doc.html
                ========================================
                """);
    }
}
