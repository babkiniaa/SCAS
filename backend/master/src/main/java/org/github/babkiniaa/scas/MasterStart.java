package org.github.babkiniaa.scas;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
public class MasterStart {
    public static void main(String[] args) {
        SpringApplication.run(MasterStart.class, args);

    }

}
