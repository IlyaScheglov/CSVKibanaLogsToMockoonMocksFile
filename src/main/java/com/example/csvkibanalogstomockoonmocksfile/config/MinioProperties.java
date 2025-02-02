package com.example.csvkibanalogstomockoonmocksfile.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    String minioUrl;

    String loginKey;

    String passwordKey;

    String bucketName;
}
