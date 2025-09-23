package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.service.SftpConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sftp-configs")
@RequiredArgsConstructor
public class SftpConfigRestController {

    private final SftpConfigService sftpConfigService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SftpConfigResponse>>> getAllSftpConfigs() {
        return ApiResponse.success(HttpStatus.OK, "SFTP Configs Retrieved Successfully",
                sftpConfigService.getAllSftpConfigs());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SftpConfigResponse>> createSftpConfig(@Valid @RequestBody SftpConfigRequest sftpConfigRequest) {
        return ApiResponse.success(HttpStatus.CREATED, "SFTP Config Created",
                sftpConfigService.createSftpConfig(sftpConfigRequest));
    }

    @PatchMapping("/{sftpConfigId}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateSftpConfig(@PathVariable Long sftpConfigId) {
        return null;
    }

    @PatchMapping("/{sftpConfigId}/activate")
    public ResponseEntity<ApiResponse<String>> activateSftpConfig(@PathVariable Long sftpConfigId) {
        return null;
    }

}
