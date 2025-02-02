package com.example.csvkibanalogstomockoonmocksfile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String minioUrl;

    private String loginKey;

    private String passwordKey;

    private String bucketName;
}
