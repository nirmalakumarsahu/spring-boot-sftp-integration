package com.sahu.springboot.basics.controller.rest;

import com.sahu.springboot.basics.dto.ApiResponse;
import com.sahu.springboot.basics.util.SftpKeyConvertorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/sftp-key-configs")
public class SftpKeyUtilRestController {

    @PostMapping("/pem/convert")
    public ResponseEntity<ApiResponse<String>> convertPemKey(@RequestParam MultipartFile pemFile) {
        return ApiResponse.success(HttpStatus.OK, "PEM Key Converted Successfully",
                SftpKeyConvertorUtil.convertPemKey(pemFile));
    }

    @PostMapping("/ppk/convert")
    public ResponseEntity<ApiResponse<String>> convertPpkKey(@RequestParam MultipartFile ppkFile) {
        return ApiResponse.success(HttpStatus.OK, "PPK Key Converted Successfully",
                SftpKeyConvertorUtil.convertPpkKey(ppkFile));
    }

}
