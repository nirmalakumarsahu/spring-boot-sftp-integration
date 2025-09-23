package com.sahu.springboot.basics.service;

import com.sahu.springboot.basics.dto.SftpDirectoryResponse;

import java.util.List;

public interface FileReaderService {
    List<SftpDirectoryResponse> getAllFileNames();

    List<String> getFileNamesBySftpConfig(Long sftpConFigId);

    String getFileContent(Long sftpConfigId, String fileName);
}
