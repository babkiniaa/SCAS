package org.github.babkiniaa.scas.validatros;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "The user with this login is already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}