package com.sahu.springboot.basics.model;

import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.constant.KeyFormat;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "sftp_configs")
public class SftpConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String host;
    @Column(nullable = false)
    private Integer port;
    @Column(nullable = false)
    private String username;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthenticationType authenticationType;
    private String password;
    @Enumerated(EnumType.STRING)
    private KeyFormat keyFormat;
    @Column(columnDefinition = "TEXT")
    private String privateKey;
    private String passphrase;
    @Column(nullable = false)
    private String remoteDirectory;
    @Column(nullable = false)
    private String salt;
    private Boolean active;
}
