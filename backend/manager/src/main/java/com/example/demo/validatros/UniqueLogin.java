package com.example.demo.validatros;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = UniqueLoginValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {
    String message() default "Пользователь с таким login уже зарегестрирован";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}