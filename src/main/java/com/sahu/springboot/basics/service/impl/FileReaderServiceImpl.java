package com.sahu.springboot.basics.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.exception.SftpConnectionException;
import com.sahu.springboot.basics.operation.SftpConnectionHandler;
import com.sahu.springboot.basics.service.FileReaderService;
import com.sahu.springboot.basics.service.SftpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileReaderServiceImpl implements FileReaderService {

    private final SftpConfigService sftpConfigService;
    private final SftpConnectionHandler sftpConnectionHandler;

    @Override
    public Map<String, String> readAllFiles() {
        log.info("Reading all files from all active SFTP Configs");
        List<SftpConfigResponse> sftpConfigResponseList = sftpConfigService.getAllDecryptedSftpConfigs();
        if (Objects.isNull(sftpConfigResponseList) || sftpConfigResponseList.isEmpty()) {
            throw new SftpConnectionException("No active SFTP Config found to read files");
        }

        sftpConfigResponseList.stream().filter(Objects::nonNull).map(sftpConfigResponse -> {
            ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);

        }).toMap();

        return ;
    }

    @Override
    public String readFileByName(String fileName) {
        return "";
    }

}
