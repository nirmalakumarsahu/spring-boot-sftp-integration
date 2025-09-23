package com.sahu.springboot.basics.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RemoteDirectoryValidator implements ConstraintValidator<ValidRemoteDirectory, String> {

    @Override
    public boolean isValid(String remoteDirectory, ConstraintValidatorContext context) {
        if (Objects.isNull(remoteDirectory) || remoteDirectory.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Remote directory is required")
                    .addConstraintViolation();
            return false;
        }

        if (!remoteDirectory.startsWith("/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("remoteDirectory must start with '/'")
                    .addConstraintViolation();
            return false;
        }

        if (remoteDirectory.endsWith("/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("remoteDirectory must not end with '/'")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
