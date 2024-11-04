package org.github.babkiniaa.scas.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для настройки клиента MinIO.
 * <p>
 * Этот класс предоставляет настройки для создания экземпляра {@link MinioClient}
 * с использованием свойств, определенных в {@link MinioProperties}.
 * Клиент MinIO используется для взаимодействия с MinIO сервером для операций
 * хранения и управления объектами.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    /**
     * Создает и настраивает {@link MinioClient} с использованием параметров,
     * определенных в {@link MinioProperties}.
     * <p>
     * Метод устанавливает конечную точку сервиса MinIO и учетные данные доступа
     * на основе предоставленных свойств.
     * </p>
     *
     * @return настроенный экземпляр {@link MinioClient} для работы с MinIO сервером
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}