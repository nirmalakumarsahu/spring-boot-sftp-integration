package com.sahu.springboot.basics.validation;

import com.sahu.springboot.basics.constant.AppConstants;
import com.sahu.springboot.basics.constant.AuthenticationType;
import com.sahu.springboot.basics.constant.KeyFormat;
import com.sahu.springboot.basics.dto.SftpConfigRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class SftpConfigAuthValidator implements ConstraintValidator<ValidSftpConfigAuth, SftpConfigRequest> {

    @Override
    public boolean isValid(SftpConfigRequest sftpConfigRequest, ConstraintValidatorContext context) {
        //If authenticationType is PASSWORD, password must be provided
        if (!(sftpConfigRequest.authenticationType().equals(AuthenticationType.PRIVATE_KEY) ||
                sftpConfigRequest.authenticationType().equals(AuthenticationType.PASSWORD))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("authenticationType must be PRIVATE_KEY or PASSWORD, other values are not allowed")
                    .addPropertyNode("authenticationType")
                    .addConstraintViolation();
            return false;
        }

        //If authenticationType is PASSWORD, password must be provided
        if (sftpConfigRequest.authenticationType().equals(AuthenticationType.PASSWORD) &&
                (Objects.isNull(sftpConfigRequest.password()) || sftpConfigRequest.password().isBlank())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("password must be provided when authenticationType is PASSWORD")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        //If authenticationType is PRIVATE_KEY, keyFormat must be provided and must be either PEM or PPK
        if (sftpConfigRequest.authenticationType().equals(AuthenticationType.PRIVATE_KEY)) {
            // keyFormat must be provided
            if (Objects.isNull(sftpConfigRequest.keyFormat())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("keyFormat must be provided when authenticationType is PRIVATE_KEY")
                        .addPropertyNode("keyFormat")
                        .addConstraintViolation();
                return false;
            }

            // keyFormat must be either PEM or PPK
            if (!(sftpConfigRequest.keyFormat().equals(KeyFormat.PEM) ||
                    sftpConfigRequest.keyFormat().equals(KeyFormat.PPK))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("keyFormat must be PEM or PPK, other values are not allowed")
                        .addPropertyNode("keyFormat")
                        .addConstraintViolation();
                return false;
            }

            // privateKey must be provided
            if (Objects.isNull(sftpConfigRequest.privateKey())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("privateKey must be provided when authenticationType is PRIVATE_KEY")
                        .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                        .addConstraintViolation();
                return false;
            }

            //Validate privateKey based on keyFormat
            //If keyFormat is PEM, privateKey must be a valid PEM key
            if (sftpConfigRequest.keyFormat().equals(KeyFormat.PEM)) {
               return checkValidPemKey(sftpConfigRequest, context);
            }

            //If keyFormat is PPK, privateKey must be a valid PPK key
            return checkValidPpkKey(sftpConfigRequest, context);
        }

        return true;
    }

    private boolean checkValidPemKey(SftpConfigRequest sftpConfigRequest,  ConstraintValidatorContext context) {
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
    }

    private boolean checkValidPpkKey(SftpConfigRequest sftpConfigRequest,  ConstraintValidatorContext context) {
        byte[] decodedBytes = Base64.getDecoder().decode(sftpConfigRequest.privateKey());
        String decodedPrivateKey = new String(decodedBytes, StandardCharsets.UTF_8);

        if (decodedPrivateKey.isBlank() || !decodedPrivateKey.startsWith(AppConstants.PPK_KEY_BEGIN) ||
                !decodedPrivateKey.contains(AppConstants.PPK_KEY_PUBLIC_LINES) ||
                !decodedPrivateKey.contains(AppConstants.PPK_KEY_PRIVATE_LINES)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Not a valid PEM key")
                    .addPropertyNode(AppConstants.PROPERTY_PRIVATE_KEY)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
