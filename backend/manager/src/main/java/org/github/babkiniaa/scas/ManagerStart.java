package org.github.babkiniaa.scas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Основной класс приложения Spring Boot.
 *
 * <p>Этот класс служит точкой входа в приложение и включает в себя
 * конфигурацию для автоматического управления настройками Spring.
 * Также включает возможность планирования задач с помощью аннотации
 * {@link EnableScheduling}.</p>
 */
@EnableScheduling
@SpringBootApplication
public class ManagerStart {

  public static void main(String[] args) {
    SpringApplication.run(ManagerStart.class, args);
  }

}
