package com.sahu.springboot.basics.validation;

import com.sahu.springboot.basics.constant.AppConstants;
import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.constant.KeyFormat;
import com.sahu.springboot.basics.dto.SftpConfigRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class SftpConfigAuthValidator implements ConstraintValidator<ValidSftpConfigAuth, SftpConfigRequest> {

    @Override
    public boolean isValid(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        try {
            //Authentication type must be either PASSWORD or PRIVATE_KEY
            boolean passwordAuthenticationType = sftpConfigRequest.authenticationType().equals(AuthenticationType.PASSWORD.name());
            if (!passwordAuthenticationType &&
                    !sftpConfigRequest.authenticationType().equals(AuthenticationType.PRIVATE_KEY.name())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("authenticationType must be either PASSWORD or PRIVATE_KEY")
                        .addPropertyNode("authenticationType")
                        .addConstraintViolation();
                return false;
            }

            //If authenticationType is PASSWORD, keyFormat, privateKey and passphrase must be null
            if (passwordAuthenticationType) {
                return checkPasswordAuthenticationType(sftpConfigRequest, context);
            } else {
                return checkPrivateKeyAuthenticationType(sftpConfigRequest, context);
            }
        } catch (Exception ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Error while validating SFTP configuration: " + ex.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    private boolean checkPasswordAuthenticationType(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        //If authenticationType is PASSWORD, password must be provided
        if (Objects.isNull(sftpConfigRequest.password()) || sftpConfigRequest.password().isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("password must be provided when authenticationType is PASSWORD")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        //If authenticationType is PASSWORD, keyFormat, privateKey and passphrase must be null
        if (Objects.nonNull(sftpConfigRequest.keyFormat())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("keyFormat must be null when authenticationType is PASSWORD")
                    .addPropertyNode(AppConstants.PROPERTY_KEY_FORMAT)
                    .addConstraintViolation();
            return false;
        }
        if (Objects.nonNull(sftpConfigRequest.privateKey())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("privateKey must be null when authenticationType is PASSWORD")
                    .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                    .addConstraintViolation();
            return false;
        }
        if (Objects.nonNull(sftpConfigRequest.passphrase())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("passphrase must be null when authenticationType is PASSWORD")
                    .addPropertyNode("passphrase")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean checkPrivateKeyAuthenticationType(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        //If authenticationType is PRIVATE_KEY, password must be null
        if (Objects.nonNull(sftpConfigRequest.password())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("password must be null when authenticationType is PRIVATE_KEY")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        //If authenticationType is PRIVATE_KEY, keyFormat must be provided
        if (Objects.isNull(sftpConfigRequest.keyFormat())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("keyFormat must be provided when authenticationType is PRIVATE_KEY")
                    .addPropertyNode(AppConstants.PROPERTY_KEY_FORMAT)
                    .addConstraintViolation();
            return false;
        }

        //If authenticationType is PRIVATE_KEY, keyFormat must be provided and must be either PEM or PPK
        boolean isPEM = sftpConfigRequest.keyFormat().equals(KeyFormat.PEM.name());
        if (!isPEM &&
                !sftpConfigRequest.keyFormat().equals(KeyFormat.PPK.name())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("keyFormat must be either PEM or PPK when authenticationType is PRIVATE_KEY")
                    .addPropertyNode(AppConstants.PROPERTY_KEY_FORMAT)
                    .addConstraintViolation();
            return false;
        }

        // privateKey must be provided
        if (Objects.isNull(sftpConfigRequest.privateKey()) || sftpConfigRequest.privateKey().isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("privateKey must be provided when authenticationType is PRIVATE_KEY")
                    .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                    .addConstraintViolation();
            return false;
        }

        //Validate privateKey based on keyFormat
        if (isPEM) {
            //If keyFormat is PEM, privateKey must be a valid PEM key
            return checkValidPemKey(sftpConfigRequest, context);
        } else {
            //If keyFormat is PPK, privateKey must be a valid PPK key
            return checkValidPpkKey(sftpConfigRequest, context);
        }
    }

    private boolean checkValidPemKey(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(sftpConfigRequest.privateKey());
            String decodedPrivateKey = new String(decodedBytes, StandardCharsets.UTF_8);

            if (decodedPrivateKey.isBlank() || !decodedPrivateKey.startsWith(AppConstants.PEM_KEY_BEGIN) ||
                    !decodedPrivateKey.contains(AppConstants.PEM_KEY_END)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Not a valid PEM key")
                        .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (IllegalArgumentException iae) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("privateKey is not a valid Base64 encoded string")
                    .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                    .addConstraintViolation();
            return false;
        }
    }

    private boolean checkValidPpkKey(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(sftpConfigRequest.privateKey());
            String decodedPrivateKey = new String(decodedBytes, StandardCharsets.UTF_8);

            if (decodedPrivateKey.isBlank() || !decodedPrivateKey.startsWith(AppConstants.PPK_KEY_BEGIN) ||
                    !decodedPrivateKey.contains(AppConstants.PPK_KEY_PUBLIC_LINES) ||
                    !decodedPrivateKey.contains(AppConstants.PPK_KEY_PRIVATE_LINES) ||
                    !decodedPrivateKey.contains(AppConstants.PPK_KEY_PRIVATE_MAC)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Not a valid PEM key")
                        .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (IllegalArgumentException iae) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("privateKey is not a valid Base64 encoded string")
                    .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                    .addConstraintViolation();
            return false;
        }
    }

}
