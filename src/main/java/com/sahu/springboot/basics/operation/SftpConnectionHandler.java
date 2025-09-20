package com.sahu.springboot.basics.operation;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.exception.SftpException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Setter
@Slf4j
@Component
@ConfigurationProperties(prefix = "app.sftp")
public class SftpConnectionHandler {

    private Integer sessionTimeout;
    private Integer channelTimeout;

    public ChannelSftp createSftpChannel(SftpConfigResponse sftpConfigResponse) {
        try {
            JSch jSch = new JSch();

            //if authentication type is PRIVATE_KEY then set the private key
            if (sftpConfigResponse.authenticationType().equals(AuthenticationType.PRIVATE_KEY)) {
                log.debug("Private key authentication selected {}", sftpConfigResponse.privateKey());

                byte[] decodePrivateKey = Base64.getDecoder().decode(sftpConfigResponse.privateKey());
                String decodedPrivateKey = new String(decodePrivateKey, StandardCharsets.UTF_8);
                log.debug("Decoded private key: {}", decodedPrivateKey);

                jSch.addIdentity("sftpIdentity", decodedPrivateKey.getBytes(), null,
                        Objects.nonNull(sftpConfigResponse.passphrase()) ?
                                sftpConfigResponse.passphrase().getBytes(StandardCharsets.UTF_8) : null);
            }

            // create session
            Session session = jSch.getSession(sftpConfigResponse.username(), sftpConfigResponse.host(),
                    sftpConfigResponse.port());
            session.setConfig("StrictHostKeyChecking", "no");

            //if authentication type is PASSWORD then set the password
            if (sftpConfigResponse.authenticationType().equals(AuthenticationType.PASSWORD)) {
                session.setPassword(sftpConfigResponse.password());
            }

            log.info("Establishing SFTP connection to {}:{}", sftpConfigResponse.host(), sftpConfigResponse.port());
            session.connect(sessionTimeout);
            log.info("✅ SFTP session connected - session timeout: {} ms", sessionTimeout);

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect(channelTimeout);
            log.info("✅ SFTP channel connected - channel timeout: {} ms", channelTimeout);

            return channelSftp;
        } catch (Exception e) {
            log.error("Error while creating SFTP channel: {}", e.getMessage());
            throw new SftpException("Failed to create SFTP channel " + e.getMessage());
        }
    }

    public void disconnectSftpChannel(ChannelSftp channelSftp) {
        if (Objects.nonNull(channelSftp)) {
            try {
                if (channelSftp.isConnected()) {
                    channelSftp.disconnect();
                    log.info("✅ SFTP channel disconnected");
                }
                if (Objects.nonNull(channelSftp.getSession()) && channelSftp.getSession().isConnected()) {
                    channelSftp.getSession().disconnect();
                    log.info("✅ SFTP session disconnected");
                }
            } catch (Exception e) {
                log.error("Error while disconnecting SFTP channel/session: {}", e.getMessage());
            }
        }
    }

    public List<String> readAllFileNamesFromRemoteDirectory(ChannelSftp channelSftp, String remoteDirectory) {
        try {
            log.info("Reading all files from remote directory: {}", remoteDirectory);
            List<String> fileNames = channelSftp.ls(remoteDirectory).stream()
                    .filter(Objects::nonNull)
                    .filter(lsEntry -> !lsEntry.getAttrs().isDir()) // Exclude directories
                    .map(ChannelSftp.LsEntry::getFilename)
                    .toList();

            log.info("Total {} files found in remote directory: {}", fileNames.size(), remoteDirectory);
            return fileNames;
        } catch (Exception e) {
            log.error("Error while reading files from SFTP server: {}", e.getMessage());
            disconnectSftpChannel(channelSftp);
            throw new SftpException("Failed to read files from SFTP server " + e.getMessage());
        }
    }

    public String readFileContentByName(ChannelSftp channelSftp, String remoteDirectory, String fileName) {
        try {
            log.info("Reading file: {} from remote directory: {}", fileName, remoteDirectory);
            String filePath =  remoteDirectory + "/" + fileName;
            byte[] fileContentBytes = channelSftp.get(filePath).readAllBytes();
            String fileContent = new String(fileContentBytes, StandardCharsets.UTF_8);
            log.info("File: {} read successfully, size: {} bytes", fileName, fileContentBytes.length);
            return fileContent;
        } catch (Exception e) {
            log.error("Error while reading file: {} from SFTP server: {}", fileName, e.getMessage());
            disconnectSftpChannel(channelSftp);
            throw new SftpException("Failed to read file: " + fileName + " from SFTP server " + e.getMessage());
        }
    }

}
