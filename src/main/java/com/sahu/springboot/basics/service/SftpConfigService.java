package com.sahu.springboot.basics.service;

import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;

public interface SftpConfigService {
    SftpConfigResponse createSftpConfig(SftpConfigRequest sftpConfigRequest);
}
