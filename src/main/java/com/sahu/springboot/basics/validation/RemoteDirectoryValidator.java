package com.sahu.springboot.basics.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RemoteDirectoryValidator implements ConstraintValidator<ValidRemoteDirectory, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value) && value.endsWith("/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("remoteDirectory must not end with '/'")
                    .addPropertyNode("remoteDirectory")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
