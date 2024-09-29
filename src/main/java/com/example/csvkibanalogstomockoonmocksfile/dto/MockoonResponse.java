package com.example.csvkibanalogstomockoonmocksfile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class MockoonResponse {

    UUID uuid;

    String body;

    Integer latency;

    Integer statusCode;

    String label;

    List<String> headers;

    String bodyType;

    String filePath;

    String databucketID;

    Boolean sendFileAsBody;

    List<String> rules;

    String rulesOperator;

    Boolean disableTemplating;

    Boolean fallbackTo404;

    @JsonProperty("default")
    Boolean isDefault;

    String crudKey;

    List<String> callbacks;
}
