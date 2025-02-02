package com.example.csvkibanalogstomockoonmocksfile.service;

import com.example.csvkibanalogstomockoonmocksfile.config.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String mapInputStreamToFileUrlInMinio(InputStream inputStream, String name) {
        try (inputStream) {
            var bucketName = minioProperties.getBucketName();
            var fileName = UUID.randomUUID() + name + ".json";
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName).object(fileName)
                    .stream(inputStream, -1, 10485760)
                    .contentType("json")
                    .build()
            );
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(180)
                    .build());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
