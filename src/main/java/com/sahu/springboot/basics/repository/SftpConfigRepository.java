package com.sahu.springboot.basics.repository;

import com.sahu.springboot.basics.model.SftpConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SftpConfigRepository extends JpaRepository<SftpConfig, Long> {
    boolean existsByNameAndActive(String name, Boolean active);
    Optional<SftpConfig> findByNameAndActive(String name, Boolean active);
}
