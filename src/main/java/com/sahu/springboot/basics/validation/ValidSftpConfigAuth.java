package com.sahu.springboot.basics.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SftpConfigAuthValidator.class)
public @interface ValidSftpConfigAuth {
    String message() default "Invalid SFTP configuration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
