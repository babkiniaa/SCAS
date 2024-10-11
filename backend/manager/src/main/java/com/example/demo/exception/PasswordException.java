package com.example.demo.exception;

/**
 * Исключение, возникающее при ошибках, связанных с паролем.
 * Используется для информирования о проблемах, таких как несоответствие паролей
 * или недопустимые условия для пароля.
 */
public class PasswordException extends Exception {

  public PasswordException(String msg) {
    super(msg);
  }
}