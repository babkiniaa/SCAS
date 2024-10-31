package org.github.babkiniaa.scas.util;

import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для удобного сбора и обработки ошибок валидации.
 * Этот класс предоставляет методы для сбора ошибок валидации из {@link BindingResult}
 * и проверки совпадения полей пароля, добавляя сообщения об ошибках в карту, если необходимо.
 */
public class ValidCollerctor {

    /**
     * Сохраняет ошибки валидации, выявленные в объекте BindingResult,
     * в карту Map<String, String>.
     *
     * @param result Объект BindingResult, содержащий ошибки валидации.
     * @return Map<String, String>, где ключ - название поля, а значение - сообщение об ошибке.
     */
    public static Map<String, String> collectValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return errors;
    }

    /**
     * Проверяет, совпадают ли введенные пароли, и в случае несовпадения
     * добавляет ошибку в карту ошибок.
     *
     * @param errors Карта ошибок, в которую добавляется сообщение при несовпадении паролей.
     */
    public static Map<String, String> checkPasswordMatch(
            String password,
            String passwordConfirm,
            Map<String, String> errors
    ) {
        if (!password.equals(passwordConfirm)) {
            errors.put("passwordConfirm", "Passwords don't match.");
        }

        return errors;
    }
}
