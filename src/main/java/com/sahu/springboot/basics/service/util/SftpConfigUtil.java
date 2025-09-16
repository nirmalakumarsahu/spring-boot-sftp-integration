package com.sahu.springboot.basics.service.util;

import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.model.SftpConfig;
import com.sahu.springboot.basics.util.AseCryptUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SftpConfigUtil {

    public SftpConfig toSftpConfig(SftpConfigRequest sftpConfigRequest) {
        String salt = AseCryptUtil.generateSecretKey();

        return SftpConfig.builder()
                .name(sftpConfigRequest.name())
                .host(AseCryptUtil.encrypt(sftpConfigRequest.host(), salt))
                .port(sftpConfigRequest.port())
                .username(AseCryptUtil.encrypt(sftpConfigRequest.username(), salt))
                .authenticationType(sftpConfigRequest.authenticationType())
                .password(AseCryptUtil.encrypt(sftpConfigRequest.password(), salt))
                .keyFormat(sftpConfigRequest.keyFormat())
                .privateKey(AseCryptUtil.encrypt(sftpConfigRequest.privateKey(), salt))
                .passphrase(AseCryptUtil.encrypt(sftpConfigRequest.passphrase(), salt))
                .remoteDirectory(sftpConfigRequest.remoteDirectory())
                .salt(salt)
                .active(true)
                .build();
    }

    public SftpConfigResponse toSftpConfigResponse(SftpConfig sftpConfig) {
        return SftpConfigResponse.builder()
                .id(sftpConfig.getId())
                .name(sftpConfig.getName())
                .active(sftpConfig.getActive())
                .build();
    }

    public List<SftpConfigResponse> toSftpConfigResponseList(List<SftpConfig> sftpConfigList) {
        return sftpConfigList.stream()
                .map(SftpConfigUtil::toSftpConfigResponse)
                .toList();
    }

    public SftpConfigResponse toDecryptSftpConfigResponse(SftpConfig sftpConfig) {
        String salt = sftpConfig.getSalt();

        return SftpConfigResponse.builder()
                .id(sftpConfig.getId())
                .name(sftpConfig.getName())
                .host(AseCryptUtil.decrypt(sftpConfig.getHost(), salt))
                .port(sftpConfig.getPort())
                .username(AseCryptUtil.decrypt(sftpConfig.getUsername(), salt))
                .authenticationType(sftpConfig.getAuthenticationType())
                .password(AseCryptUtil.decrypt(sftpConfig.getPassword(), salt))
                .keyFormat(sftpConfig.getKeyFormat())
                .privateKey(AseCryptUtil.decrypt(sftpConfig.getPrivateKey(), salt))
                .passphrase(AseCryptUtil.decrypt(sftpConfig.getPassphrase(), salt))
                .remoteDirectory(sftpConfig.getRemoteDirectory())
                .active(sftpConfig.getActive())
                .build();
    }

}
