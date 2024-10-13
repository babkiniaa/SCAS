package org.github.babkiniaa.scas.validatros;

import org.github.babkiniaa.scas.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueLogin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findByLogin(login).isEmpty();
    }
}