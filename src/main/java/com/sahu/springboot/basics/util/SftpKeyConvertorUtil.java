package com.sahu.springboot.basics.util;

import com.sahu.springboot.basics.constant.AppConstants;
import com.sahu.springboot.basics.exception.InvalidSftpKeyFileException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@UtilityClass
public class SftpKeyConvertorUtil {

    public String convertPemKey(MultipartFile pemFile) {
        log.info("Convert PEM key started");
        String fileName = pemFile.getOriginalFilename();
        log.info("PEM File name is : {}", fileName);
        if (Objects.isNull(fileName) || !fileName.endsWith(".pem")) {
            throw new InvalidSftpKeyFileException("Invalid SFTP Key File. Please upload valid .pem file");
        }

        try {
            //read the file content and convert it to String
            byte[] pemFileByte = pemFile.getBytes();
            String pemPrivateKey = new String(pemFileByte);
            log.debug("PEM Private key content : {}", pemPrivateKey);
            log.info("Convert PEM key completed");

            if (pemPrivateKey.isBlank() || !pemPrivateKey.startsWith(AppConstants.PEM_KEY_BEGIN) ||
                    !pemPrivateKey.contains(AppConstants.PEM_KEY_END)) {
                throw new InvalidSftpKeyFileException("Invalid SFTP Key File. Please upload valid .pem file");
            }

            byte[] encodedByte = Base64.getEncoder().encode(pemFileByte);
            return new String(encodedByte, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InvalidSftpKeyFileException(e.getMessage());
        }
    }

    public String convertPpkKey(MultipartFile ppkFile) {
        log.info("Convert PPK key started");
        String fileName = ppkFile.getOriginalFilename();
        log.info("PPK File name is : {}", fileName);
        if (Objects.isNull(fileName) || !fileName.endsWith(".ppk")) {
            throw new InvalidSftpKeyFileException("Invalid SFTP Key File. Please upload valid .ppk file");
        }

        try {
            //read the file content and convert it to String
            byte[] ppkFileByte = ppkFile.getBytes();
            String ppkPrivateKey = new String(ppkFileByte);
            log.debug("PPK Private key content : {}", ppkPrivateKey);
            log.info("Convert PPK key completed");

            if (ppkPrivateKey.isBlank() || !ppkPrivateKey.startsWith(AppConstants.PPK_KEY_BEGIN) ||
                    !ppkPrivateKey.contains(AppConstants.PPK_KEY_PUBLIC_LINES) ||
                    !ppkPrivateKey.contains(AppConstants.PPK_KEY_PRIVATE_LINES)) {
                throw new InvalidSftpKeyFileException("Invalid SFTP Key File. Please upload valid .ppk file");
            }

            byte[] encodedByte = Base64.getEncoder().encode(ppkFileByte);
            return new String(encodedByte, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InvalidSftpKeyFileException(e.getMessage());
        }
    }

}
