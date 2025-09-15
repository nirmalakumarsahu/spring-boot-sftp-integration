package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.service.SftpConfigService;
import com.sahu.springboot.basics.util.SftpKeyConvertorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/sftp-configs")
@RequiredArgsConstructor
public class SftpConfigRestController {

    private final SftpConfigService sftpConfigService;

    @PostMapping
    public ResponseEntity<ApiResponse<SftpConfigResponse>> createSftpConfig(@Valid @RequestBody SftpConfigRequest sftpConfigRequest) {
        return ApiResponse.success(HttpStatus.CREATED, "SFTP Config Created",
                sftpConfigService.createSftpConfig(sftpConfigRequest));
    }

    @PostMapping("/convert-pem-key")
    public ResponseEntity<ApiResponse<String>> convertPemKey(@RequestParam MultipartFile pemFile) {
        return ApiResponse.success(HttpStatus.OK, "PEM Key Converted Successfully",
                SftpKeyConvertorUtil.convertPemKey(pemFile));
    }

    @PostMapping("/convert-ppk-key")
    public ResponseEntity<ApiResponse<String>> convertPpkKey(@RequestParam MultipartFile ppkFile) {
        return ApiResponse.success(HttpStatus.OK, "PPK Key Converted Successfully",
                SftpKeyConvertorUtil.convertPpkKey(ppkFile));
    }

}
