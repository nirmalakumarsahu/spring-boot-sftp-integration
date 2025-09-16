package com.sahu.springboot.basics.service.impl;

import com.sahu.springboot.basics.dto.SftpConfigRequest;
import com.sahu.springboot.basics.dto.SftpConfigResponse;
import com.sahu.springboot.basics.exception.SftpConfigAlreadyExistException;
import com.sahu.springboot.basics.exception.SftpConfigNotFoundException;
import com.sahu.springboot.basics.model.SftpConfig;
import com.sahu.springboot.basics.repository.SftpConfigRepository;
import com.sahu.springboot.basics.service.SftpConfigService;
import com.sahu.springboot.basics.service.util.SftpConfigUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SftpConfigServiceImpl implements SftpConfigService {

    private final SftpConfigRepository sftpConfigRepository;

    @Override
    public SftpConfigResponse createSftpConfig(SftpConfigRequest sftpConfigRequest) {
        if (sftpConfigRepository.existsByNameAndActive(sftpConfigRequest.name(), true)) {
            log.warn("SFTP Config is already exist with name {}", sftpConfigRequest.name());
            throw new SftpConfigAlreadyExistException("SFTP Config is already exist with name " + sftpConfigRequest.name());
        }

        SftpConfig sftpConfig = SftpConfigUtil.toSftpConfig(sftpConfigRequest);
        return SftpConfigUtil.toSftpConfigResponse(sftpConfigRepository.save(sftpConfig));
    }

    @Override
    public List<SftpConfigResponse> getAllSftpConfigs() {
        return SftpConfigUtil.toSftpConfigResponseList(sftpConfigRepository.findAll());
    }

    @Override
    public SftpConfigResponse getDecryptedSftpConfigByName(String name) {
        return sftpConfigRepository.findByNameAndActive(name, true)
                .map(SftpConfigUtil::toDecryptSftpConfigResponse)
                .orElseThrow(() -> new SftpConfigNotFoundException(
                        "Active SFTP Config not found with name " + name
                ));
    }


}
