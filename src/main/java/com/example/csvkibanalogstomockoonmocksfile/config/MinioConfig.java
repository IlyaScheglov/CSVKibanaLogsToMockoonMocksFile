package com.example.csvkibanalogstomockoonmocksfile.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        var minioClient = MinioClient.builder()
                .endpoint(minioProperties.getMinioUrl())
                .credentials(minioProperties.getLoginKey(), minioProperties.getPasswordKey())
                .build();
        var bucketName = minioProperties.getBucketName();

        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).objectLock(false).build());
            }
        } catch (Exception e) {
            log.error("minio bucket creation error");
        }
        return minioClient;
    }
}
