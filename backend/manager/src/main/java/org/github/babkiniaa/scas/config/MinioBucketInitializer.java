package org.github.babkiniaa.scas.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.properties.MinioProperties;
import org.springframework.stereotype.Component;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Component
@RequiredArgsConstructor
public class MinioBucketInitializer {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    @EventListener(ApplicationReadyEvent.class)
    public void ensureBucketExists() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucketName())
                .build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .build());
        }
    }
}
