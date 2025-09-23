package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.dto.SftpDirectoryResponse;
import com.sahu.springboot.basics.exception.SftpConfigNotFoundException;
import com.sahu.springboot.basics.service.FileReaderService;
import com.sahu.springboot.basics.service.SftpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sftp-files")
@RequiredArgsConstructor
public class SftpFileRestController {

    private final FileReaderService fileReaderService;
    private final SftpConfigService sftpConfigService;

    @GetMapping("/file-names")
    public ResponseEntity<ApiResponse<List<SftpDirectoryResponse>>> getAllFileNames() {
        return ApiResponse.success(HttpStatus.OK, "File names Retrieved from all Active SFTP Successfully",
                fileReaderService.getAllFileNames());
    }

    @GetMapping("/{sftpConfigId}/file-names")
    public ResponseEntity<ApiResponse<List<String>>> getFileNamesBySftpConfig(@PathVariable Long sftpConFigId) {
        if (sftpConfigService.existsById(sftpConFigId)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with id " + sftpConFigId);
        }

        return ApiResponse.success(HttpStatus.OK, "File names Retrieved from given SFTP Successfully",
                fileReaderService.getFileNamesBySftpConfig(sftpConFigId));
    }

    @GetMapping("/{sftpConfigId}/{fileName}")
    public ResponseEntity<ApiResponse<String>> getFileContent(Long sftpConfigId, String fileName) {
        if (sftpConfigService.existsById(sftpConfigId)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with id " + sftpConfigId);
        }

        return ApiResponse.success(HttpStatus.OK, "File Contents Retrieved Successfully",
                fileReaderService.getFileContent(sftpConfigId, fileName));
    }

}
