package com.sahu.springboot.basics.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.exception.SftpException;
import com.sahu.springboot.basics.operation.SftpConnectionHandler;
import com.sahu.springboot.basics.service.FileReaderService;
import com.sahu.springboot.basics.service.SftpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileReaderServiceImpl implements FileReaderService {

    private final SftpConfigService sftpConfigService;
    private final SftpConnectionHandler sftpConnectionHandler;

    @Override
    public Map<String, List<String>> getAllFileNames() {
        log.info("Getting all file names from all active SFTP Configs");
        List<SftpConfigResponse> sftpConfigResponseList = sftpConfigService.getAllDecryptedSftpConfigs();
        if (Objects.isNull(sftpConfigResponseList) || sftpConfigResponseList.isEmpty()) {
            throw new SftpException("No active SFTP Config found to read files");
        }

        return sftpConfigResponseList.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        SftpConfigResponse::name,
                        sftpConfigResponse -> {
                            ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);
                            return sftpConnectionHandler.readAllFileNamesFromRemoteDirectory(channelSftp, sftpConfigResponse.remoteDirectory());
                        })
                );
    }

    @Override
    public Map<String, String> readAllFileContents(String sftpConfigName) {
        log.info("Reading all files from all active SFTP Configs");
        SftpConfigResponse sftpConfigResponse = sftpConfigService.getDecryptedSftpConfigByName(sftpConfigName);

        ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);

        //get all file names from the remote directory
        List<String> fileNames = sftpConnectionHandler.readAllFileNamesFromRemoteDirectory(channelSftp, sftpConfigResponse.remoteDirectory());
        if (Objects.isNull(fileNames) || fileNames.isEmpty()) {
            log.warn("No files found in the remote directory {}", sftpConfigResponse.remoteDirectory());
            throw new SftpException("No files found in the remote directory " + sftpConfigResponse.remoteDirectory());
        }

        return fileNames.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(
                                fileName -> fileName,
                                fileName -> sftpConnectionHandler.readFileContentByName(channelSftp,
                                        sftpConfigResponse.remoteDirectory(), fileName)
                        )
                );
    }

    @Override
    public String readFileByName(String sftpConfigName, String fileName) {
        log.info("Reading file {} from SFTP Config {}", fileName, sftpConfigName);
        SftpConfigResponse sftpConfigResponse = sftpConfigService.getDecryptedSftpConfigByName(sftpConfigName);

        ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);

        return sftpConnectionHandler.readFileContentByName(channelSftp, sftpConfigResponse.remoteDirectory(), fileName);
    }

}
