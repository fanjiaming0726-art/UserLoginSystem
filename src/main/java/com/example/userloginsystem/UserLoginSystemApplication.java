package com.example.userloginsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserLoginSystemApplication {

    public static void main(String[] args) {

        // 读取 .env 文件
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // 设置为系统环境变量
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(UserLoginSystemApplication.class, args);
    }

}
