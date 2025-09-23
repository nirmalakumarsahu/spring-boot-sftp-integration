package com.sahu.springboot.basics.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RemoteDirectoryValidator.class)
public @interface ValidRemoteDirectory {
    String message() default "Invalid remote directory";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
