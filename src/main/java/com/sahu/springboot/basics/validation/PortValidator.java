package com.sahu.springboot.basics.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PortValidator implements ConstraintValidator<ValidPort, Integer> {

    @Override
    public boolean isValid(Integer port, ConstraintValidatorContext context) {
        if (Objects.isNull(port)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Port is required")
                    .addConstraintViolation();
            return false;
        }

        return port == 22 || (port >= 1024 && port <= 65535);
    }

}
