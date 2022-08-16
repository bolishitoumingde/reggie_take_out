package com.example.take_out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeOutApplication.class, args);
        log.info("启动成功");
    }

}
