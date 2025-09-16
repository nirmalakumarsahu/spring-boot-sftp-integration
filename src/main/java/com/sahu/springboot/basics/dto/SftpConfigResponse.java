package com.sahu.springboot.basics.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.constant.KeyFormat;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SftpConfigResponse(
        Long id,
        String name,
        String host,
        Integer port,
        String username,
        AuthenticationType authenticationType,
        String password,
        KeyFormat keyFormat,
        String privateKey,
        String passphrase,
        String remoteDirectory,
        Boolean active
) {
}
