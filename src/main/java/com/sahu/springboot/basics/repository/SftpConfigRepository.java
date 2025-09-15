package com.sahu.springboot.basics.repository;

import com.sahu.springboot.basics.model.SftpConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SftpConfigRepository extends JpaRepository<SftpConfig, Long> {
    boolean existsByName(String name);
}
