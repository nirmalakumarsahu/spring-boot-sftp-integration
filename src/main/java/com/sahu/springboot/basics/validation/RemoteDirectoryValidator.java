package com.sahu.springboot.basics.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RemoteDirectoryValidator implements ConstraintValidator<ValidRemoteDirectory, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!value.startsWith("/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("remoteDirectory must start with '/'")
                    .addPropertyNode("remoteDirectory")
                    .addConstraintViolation();
            return false;
        }

        if (value.endsWith("/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("remoteDirectory must not end with '/'")
                    .addPropertyNode("remoteDirectory")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
