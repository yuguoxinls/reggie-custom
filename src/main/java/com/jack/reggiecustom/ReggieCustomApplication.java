package com.jack.reggiecustom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
//@EnableTransactionManagement
//@EnableCaching
@EnableScheduling
@Slf4j
public class ReggieCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieCustomApplication.class, args);
        log.info("项目启动成功...");
    }

}
