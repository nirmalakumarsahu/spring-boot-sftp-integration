package com.sahu.springboot.basics.validation;


import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SftpConfigAuthValidator.class)
@Documented
public @interface ValidSftpConfigAuth {
}
