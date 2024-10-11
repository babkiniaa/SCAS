package org.github.babkiniaa.scas.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Конфигурация безопасности для Spring Security,
 * включающая CORS и обработку авторизационных запросов.
 * Этот класс также отвечает за настройку безопасности для HTTP-запросов, таких как аутентификация,
 * авторизация и выход из системы.
 */
@Configuration
@EnableWebSecurity
public class Config implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    /**
     * Конфигурирует цепочку фильтров безопасности для обработки HTTP-запросов.
     * Включает CORS, отключает CSRF, настраивает авторизацию по URL
     * и обрабатывает логику выхода из системы.
     * - Разрешает доступ к маршрутам "/auth/**" и "/password/**" без авторизации.
     * - Требует авторизацию для всех остальных запросов.
     * - Настраивает выход из системы через "/logout",
     * с последующим перенаправлением на "/auth/login".
     *
     * @param httpSecurity объект для настройки параметров безопасности.
     * @return настроенная цепочка фильтров безопасности.
     * @throws Exception если происходит ошибка конфигурации безопасности.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth/**", "/password/**").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .permitAll()
                );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
