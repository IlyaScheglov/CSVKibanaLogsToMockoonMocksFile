package com.example.csvkibanalogstomockoonmocksfile.controller;

import com.example.csvkibanalogstomockoonmocksfile.service.CsvReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/csv-to-mockoon")
public class CsvController {

    private final CsvReaderService csvReaderService;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<String> getCsvLog(@RequestPart MultipartFile input, @RequestParam String output,
                                            @RequestParam String name) {
        return ResponseEntity.ok(csvReaderService.readLogsFromCSV(input, output, name));
    }
}
