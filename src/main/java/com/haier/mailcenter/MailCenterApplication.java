package com.haier.mailcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.haier.mailcenter.dao")
public class MailCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailCenterApplication.class, args);
    }

}
