package com.sahu.springboot.basics.service;

import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;

import java.util.List;

public interface SftpConfigService {
    SftpConfigResponse createSftpConfig(SftpConfigRequest sftpConfigRequest);
    List<SftpConfigResponse> getAllSftpConfigs();
}
