package com.example.csvkibanalogstomockoonmocksfile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class MockoonRoute {

    UUID uuid;

    String type;

    String documentation;

    String method;

    String endpoint;

    List<MockoonResponse> responses;

    String responseMode;
}
