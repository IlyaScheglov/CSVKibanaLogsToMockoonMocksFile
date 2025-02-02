package com.example.csvkibanalogstomockoonmocksfile;

import com.example.csvkibanalogstomockoonmocksfile.config.MinioConfig;
import com.example.csvkibanalogstomockoonmocksfile.config.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MinioProperties.class)
public class CsvKibanaLogsToMockoonMocksFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvKibanaLogsToMockoonMocksFileApplication.class, args);
    }

}
