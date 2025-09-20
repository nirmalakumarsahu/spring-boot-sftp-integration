package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.service.FileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/file-readers")
@RequiredArgsConstructor
public class FileReaderRestController {

    private final FileReaderService fileReaderService;

    @GetMapping("/read-all-files")
    public ResponseEntity<ApiResponse<Map<String, String>>> readAllFiles() {
        return ApiResponse.success(fileReaderService.readAllFiles());
    }

    @GetMapping("/read-file-by-name")
    public ResponseEntity<ApiResponse<String>> readFileByName(@RequestParam String fileName) {
        return null;
    }

    //download the file by name
    public ResponseEntity<ApiResponse<String>> downloadFileByName(@RequestParam String fileName) {
        return null;
    }

}
