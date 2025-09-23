package com.sahu.springboot.basics.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PortValidator.class)
public @interface ValidPort {
    String message() default "Port must be 22 or between 1024 and 65535";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
