package com.sahu.springboot.basics.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SftpDirectoryResponse(
        String sftpConfigName,
        List<String> fileNames
)
{

}
