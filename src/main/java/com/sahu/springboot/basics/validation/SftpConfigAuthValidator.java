package com.sahu.springboot.basics.validation;

import com.sahu.springboot.basics.dto.SftpConfigRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SftpConfigAuthValidator implements ConstraintValidator<ValidSftpConfigAuth, SftpConfigRequest> {

    @Override
    public boolean isValid(SftpConfigRequest value, ConstraintValidatorContext context) {
        return true;
    }

}
