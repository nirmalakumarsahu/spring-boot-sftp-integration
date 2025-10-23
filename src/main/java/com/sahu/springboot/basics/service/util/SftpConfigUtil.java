package com.sahu.springboot.basics.service.util;

import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.constant.KeyFormat;
import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.model.SftpConfig;
import com.sahu.springboot.basics.util.AesCryptUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SftpConfigUtil {

    public SftpConfig toSftpConfig(SftpConfigRequest sftpConfigRequest) {
        String salt = AesCryptUtil.generateSecretKey();

        return SftpConfig.builder()
                .name(sftpConfigRequest.name())
                .host(AesCryptUtil.encrypt(sftpConfigRequest.host(), salt))
                .port(sftpConfigRequest.port())
                .username(AesCryptUtil.encrypt(sftpConfigRequest.username(), salt))
                .authenticationType(AuthenticationType.valueOf(sftpConfigRequest.authenticationType()))
                .password(AesCryptUtil.encrypt(sftpConfigRequest.password(), salt))
                .keyFormat(KeyFormat.valueOf(sftpConfigRequest.keyFormat()))
                .privateKey(AesCryptUtil.encrypt(sftpConfigRequest.privateKey(), salt))
                .passphrase(AesCryptUtil.encrypt(sftpConfigRequest.passphrase(), salt))
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
                .host(AesCryptUtil.decrypt(sftpConfig.getHost(), salt))
                .port(sftpConfig.getPort())
                .username(AesCryptUtil.decrypt(sftpConfig.getUsername(), salt))
                .authenticationType(sftpConfig.getAuthenticationType())
                .password(AesCryptUtil.decrypt(sftpConfig.getPassword(), salt))
                .keyFormat(sftpConfig.getKeyFormat())
                .privateKey(AesCryptUtil.decrypt(sftpConfig.getPrivateKey(), salt))
                .passphrase(AesCryptUtil.decrypt(sftpConfig.getPassphrase(), salt))
                .remoteDirectory(sftpConfig.getRemoteDirectory())
                .active(sftpConfig.getActive())
                .build();
    }

    public List<SftpConfigResponse> toDecryptSftpConfigResponseList(List<SftpConfig> sftpConfigList) {
        return sftpConfigList.stream().map(SftpConfigUtil::toDecryptSftpConfigResponse).toList();
    }

}
