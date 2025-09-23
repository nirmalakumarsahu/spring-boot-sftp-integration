package com.sahu.springboot.basics.dto;

import com.sahu.springboot.basics.validation.ValidPort;
import com.sahu.springboot.basics.validation.ValidRemoteDirectory;
import com.sahu.springboot.basics.validation.ValidSftpConfigAuth;
import jakarta.validation.constraints.NotBlank;

@ValidSftpConfigAuth
public record SftpConfigRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Host is required")
        String host,
        @ValidPort
        Integer port,
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Authentication type is required")
        String authenticationType,
        String password,
        String keyFormat,
        String privateKey,
        String passphrase,
        @ValidRemoteDirectory
        String remoteDirectory
)
{
}
