package com.sahu.springboot.basics.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.dto.SftpDirectoryResponse;
import com.sahu.springboot.basics.exception.SftpConfigNotFoundException;
import com.sahu.springboot.basics.exception.SftpException;
import com.sahu.springboot.basics.operation.SftpConnectionHandler;
import com.sahu.springboot.basics.service.FileReaderService;
import com.sahu.springboot.basics.service.SftpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileReaderServiceImpl implements FileReaderService {

    private final SftpConfigService sftpConfigService;
    private final SftpConnectionHandler sftpConnectionHandler;

    @Override
    public List<SftpDirectoryResponse> getAllFileNames() {
        log.info("Getting all file names from all active SFTP Configs");
        List<SftpConfigResponse> sftpConfigResponseList = sftpConfigService.getAllDecryptedSftpConfigs();
        if (Objects.isNull(sftpConfigResponseList) || sftpConfigResponseList.isEmpty()) {
            throw new SftpException("No active SFTP Config found to read files");
        }

        return sftpConfigResponseList.stream().filter(Objects::nonNull)
                .map(sftpConfigResponse -> {
                    ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);
                    List<String> fileNames = sftpConnectionHandler.readAllFileNamesFromRemoteDirectory(channelSftp, sftpConfigResponse.remoteDirectory());
                    return SftpDirectoryResponse.builder()
                            .sftpConfigName(sftpConfigResponse.name())
                            .fileNames(fileNames)
                            .build();
                }).toList();
    }

    @Override
    public List<String> getFileNamesBySftpConfig(Long id) {
        log.info("getSpecificSftpFileNames Started");
        if (!sftpConfigService.existsById(id)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with id " + id);
        }

        SftpConfigResponse sftpConfigResponse = sftpConfigService.getDecryptedSftpConfigById(id);
        ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);

        //get all file names from the remote directory
        return sftpConnectionHandler.readAllFileNamesFromRemoteDirectory(channelSftp, sftpConfigResponse.remoteDirectory());
    }

    @Override
    public String getFileContent(Long sftpConfigId, String fileName) {
        log.info("Reading file {} from SFTP Config {}", fileName, sftpConfigId);
        if (!sftpConfigService.existsById(sftpConfigId)) {
            throw new SftpConfigNotFoundException("Active SFTP Config not found with id " + sftpConfigId);
        }

        SftpConfigResponse sftpConfigResponse = sftpConfigService.getDecryptedSftpConfigById(sftpConfigId);

        ChannelSftp channelSftp = sftpConnectionHandler.createSftpChannel(sftpConfigResponse);

        return sftpConnectionHandler.readFileContentByName(channelSftp, sftpConfigResponse.remoteDirectory(), fileName);
    }

}
