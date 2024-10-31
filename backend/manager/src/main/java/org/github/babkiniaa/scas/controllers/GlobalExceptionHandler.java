package org.github.babkiniaa.scas.controllers;

import jakarta.mail.MessagingException;
import org.github.babkiniaa.scas.exception.NotFoundUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@CrossOrigin(origins = "http://localhost:9000")
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение, возникающее при использовании неподдерживаемой кодировки.
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleUnsupportedEncodingException(UnsupportedEncodingException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unsupported encoding.");
        errorResponse.put("details", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, связанные с ошибками при отправке сообщений (например, email).
     */
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<Map<String, String>> handleMessagingException(MessagingException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An error occurred when sending the message.");
        errorResponse.put("details", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Обрабатывает исключение, возникающее, если пользователь не найден в системе.
     */
    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNotFoundUserException(NotFoundUserException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "The user was not found.");
        errorResponse.put("details", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает общие исключения, возникающие при ошибках, например, обработки запроса.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An error occurred on the server.");
        errorResponse.put("details", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}