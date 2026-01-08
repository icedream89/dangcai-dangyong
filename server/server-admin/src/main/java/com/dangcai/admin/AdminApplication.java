package com.dangcai.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台管理系统启动类
 *
 * @author dangcai
 * @since 2026-01-07
 */
@SpringBootApplication(scanBasePackages = "com.dangcai")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("""

                ========================================
                   当才当用 - 后台管理系统启动成功！
                   访问地址: http://localhost:8080
                   Knife4j文档: http://localhost:8080/doc.html
                ========================================
                """);
    }
}
