package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.exception.SftpConfigNotFoundException;
import com.sahu.springboot.basics.service.FileReaderService;
import com.sahu.springboot.basics.service.SftpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/file-readers")
@RequiredArgsConstructor
public class FileReaderRestController {

    private final FileReaderService fileReaderService;
    private final SftpConfigService sftpConfigService;

    @GetMapping("/read-all-file-names")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> getAllFileNames() {
        return ApiResponse.success(HttpStatus.OK, "File Names Retrieved Successfully",
                fileReaderService.getAllFileNames());
    }

    @GetMapping("/read-all-file-contents")
    public ResponseEntity<ApiResponse<Map<String, String>>> readAllFileContents(@RequestParam String sftpConfigName) {
        if (!sftpConfigService.existsByName(sftpConfigName)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with name " + sftpConfigName);
        }

        return ApiResponse.success(HttpStatus.OK, "File Contents Retrieved Successfully",
                fileReaderService.readAllFileContents(sftpConfigName));
    }

    @GetMapping("/read-file-content")
    public ResponseEntity<ApiResponse<String>> readFileContent(@RequestParam String sftpConfigName, @RequestParam String fileName) {
        if (!sftpConfigService.existsByName(sftpConfigName)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with name " + sftpConfigName);
        }

        return ApiResponse.success(HttpStatus.OK, "File Content Retrieved Successfully",
                fileReaderService.readFileByName(sftpConfigName, fileName));
    }

}
