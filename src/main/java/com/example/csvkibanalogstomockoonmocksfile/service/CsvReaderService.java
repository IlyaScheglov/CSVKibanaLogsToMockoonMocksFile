package com.example.csvkibanalogstomockoonmocksfile.service;

import com.example.csvkibanalogstomockoonmocksfile.dto.CsvLogDto;
import com.example.csvkibanalogstomockoonmocksfile.dto.MockoonResponse;
import com.example.csvkibanalogstomockoonmocksfile.dto.MockoonRootChildren;
import com.example.csvkibanalogstomockoonmocksfile.dto.MockoonRoute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String readLogsFromCSV(MultipartFile inputFile, String outputPath, String name) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile.getInputStream(), StandardCharsets.UTF_8))) {
            List<CsvLogDto> logs = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(strToLogDto(line));
            }
            return finalizeMockoonOperation(logs, outputPath, name);
        } catch (Exception e) {
            throw new RuntimeException("Failed reading csv because: " + e.getMessage());
        }
    }

    @SneakyThrows
    private CsvLogDto strToLogDto(String str) {
        int counter = 0;
        int indexFirst = 0;
        int indexSecond = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\"') {
                if (counter == 2) {
                    indexFirst = i;
                    counter++;
                }
                else if(counter == 3) {
                    indexSecond = i;
                    break;
                }
                else {
                    counter++;
                }
            }
        }

        String methodAndUrl = str.substring(indexFirst + 1, indexSecond);
        String[] strParts = methodAndUrl.split(" ");
        String method = strParts[0].toLowerCase();
        String url = strParts[1];

        int indexOneOfStatus = indexSecond + 2;
        int indexTwoOfStatus = indexOneOfStatus + 3;
        Integer status = Integer.parseInt(str.substring(indexOneOfStatus, indexTwoOfStatus));

        int secondCounter = 0;
        int finalIndex = 0;
        for (int j = indexTwoOfStatus; j < str.length(); j++) {
            if (str.charAt(j) == '\"') {
                if (secondCounter == 2) {
                    finalIndex = j + 1;
                    break;
                }
                else {
                    secondCounter++;
                }
            }
        }
        String json = str.substring(finalIndex, str.length() - 1).replaceAll("\"\"", "\"");
        return new CsvLogDto(method, url, status, json);
    }

    @SneakyThrows
    private String finalizeMockoonOperation(List<CsvLogDto> logs, String filePath, String name) {
        try {
            List<MockoonRoute> routes = logs.stream()
                    .map(this::mapLogToMockoonRoute)
                    .collect(Collectors.toList());
            List<MockoonRootChildren> childrens = routes.stream()
                    .map(this::generateRootChildren)
                    .collect(Collectors.toList());

            JsonNode rootNode = objectMapper.readTree(new ClassPathResource("mockoon_example.json").getInputStream());

            rootNode = ((ObjectNode) rootNode).set("uuid", objectMapper.valueToTree(UUID.randomUUID()));
            rootNode = ((ObjectNode) rootNode).set("name", objectMapper.valueToTree(name));
            rootNode = ((ObjectNode) rootNode).set("routes", objectMapper.valueToTree(routes));
            rootNode = ((ObjectNode) rootNode).set("rootChildren", objectMapper.valueToTree(childrens));

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                objectMapper.writeValue(fileWriter, rootNode);
            } catch (Exception e) {
                throw new RuntimeException("Cannot save file because: " + e.getMessage());
            }

            return "Good";
        } catch (Exception e) {
            return "Bad";
        }
    }

    private MockoonRoute mapLogToMockoonRoute(CsvLogDto log) {
        return MockoonRoute.builder()
                .responseMode(null)
                .uuid(UUID.randomUUID())
                .type("http")
                .documentation("")
                .method(log.getMethod())
                .endpoint(trimUrl(log.getUrl()))
                .responses(List.of(mapToMockoonResponse(log.getStatus(), log.getJsonResponse())))
                .build();
    }

    private MockoonResponse mapToMockoonResponse(Integer status, String jsonResponse) {
        return MockoonResponse.builder()
                .uuid(UUID.randomUUID())
                .body(jsonResponse)
                .latency(0)
                .statusCode(status)
                .label("")
                .headers(Collections.emptyList())
                .bodyType("INLINE")
                .filePath("")
                .databucketID("")
                .sendFileAsBody(false)
                .rules(Collections.emptyList())
                .rulesOperator("OR")
                .disableTemplating(false)
                .fallbackTo404(false)
                .isDefault(true)
                .crudKey("id")
                .callbacks(Collections.emptyList())
                .build();
    }

    private MockoonRootChildren generateRootChildren(MockoonRoute route) {
        return MockoonRootChildren.builder()
                .uuid(route.getUuid())
                .type("route")
                .build();
    }

    private String trimUrl(String url) {
        int apiIndex = url.indexOf("/api");

        if (apiIndex != -1) {
            int endIndex = url.indexOf("?");

            if (endIndex == -1) {
                endIndex = url.length();
            }
            return url.substring(apiIndex, endIndex);
        }

        return url;
    }
}
