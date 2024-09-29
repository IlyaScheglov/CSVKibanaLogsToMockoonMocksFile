package com.example.csvkibanalogstomockoonmocksfile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class CsvLogDto {

    String method;

    String url;

    Integer status;

    String jsonResponse;
}
