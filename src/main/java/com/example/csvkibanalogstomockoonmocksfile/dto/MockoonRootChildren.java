package com.example.csvkibanalogstomockoonmocksfile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class MockoonRootChildren {

    String type;

    UUID uuid;
}
