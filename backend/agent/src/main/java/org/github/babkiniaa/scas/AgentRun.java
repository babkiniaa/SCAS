package org.github.babkiniaa.scas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class AgentRun {

    public static void main(String[] args) {
        SpringApplication.run(AgentRun.class, args);

    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

}