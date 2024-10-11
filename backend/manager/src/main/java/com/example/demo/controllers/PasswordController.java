package com.example.demo.controllers;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.exception.PasswordException;
import com.example.demo.service.EmailService;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Контроллер для изменения пароля пользователя.
 * Отвечает за отправку кода подтверждения на email, проверку кода и обновление пароля.
*/
@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

  private final EmailService emailService;

  private final UserService userService;

  private final TokenService tokenService;

  /**
   * Показывает форму для ввода email, на который будет отправлено письмо с кодом для смены пароля.
   *
   * @param model объект для передачи данных на страницу
   * @return имя шаблона для формы ввода email
 */
  @GetMapping("/change")
  public String showFormChangePassword(Model model) {
    model.addAttribute("email", new RegistrationDto());
    return "form-email";
  }

  /**
   * Обрабатывает запрос на смену пароля отправляет письмо с кодом подтверждения на указанный email.
   *
   * @param email DTO с email для смены пароля
   * @param request запрос, содержащий информацию для формирования URL
   * @return имя шаблона для страницы авторизации
   * @throws MessagingException при ошибке отправки письма
   * @throws UnsupportedEncodingException при ошибке кодировки email
 */
  @PostMapping("/change")
  public String verifyEmailForChangePassword(
          @ModelAttribute("email") ChangePasswordDto email,
          HttpServletRequest request
  ) throws MessagingException, UnsupportedEncodingException {
    String code = userService.changePassword(email.getEmail());
    emailService.sendVerificationPassword(email.getEmail(), code, request);
    return "auth";
  }

  /**
   * Показывает форму для смены пароля, если код подтверждения действителен.
   *
   * @param code код подтверждения, отправленный в письме
   * @param model объект для передачи данных на страницу
   * @return имя шаблона для страницы смены пароля или авторизации в случае ошибки
   */
  @GetMapping("/change-password")
  public String changePasswordVerify(@Param("code") String code, Model model) {
    model.addAttribute("changePassword", new ChangePasswordDto());
    if (userService.verifyForChangePassword(code)) {
      model.addAttribute("verify-code", code);
      return "change-password";
    } else {
      return "auth";
    }
  }

  /**
   * Обрабатывает смену пароля проверяет совпадение введенных паролей обновляяя пароль пользователя.
   *
   * @param changePasswordDto DTO с новыми паролями
   * @param request запрос, содержащий код подтверждения
   * @return имя шаблона для страницы авторизации
   * @throws PasswordException если пароли не совпадают
   */
  @PostMapping("/change-password")
  public String changePassword(
          @ModelAttribute("changePassword") ChangePasswordDto changePasswordDto,
          HttpServletRequest request
  ) throws PasswordException {
    User user = tokenService.getByVerifyCode(request.getParameter("verify-code")).getUser();
    if (changePasswordDto.getPassword().equals(changePasswordDto.getPasswordConfirm())) {
      userService.changePasswordUser(user, changePasswordDto.getPassword());
    } else {
      throw new PasswordException("passwords don't match");
    }
    return "auth";
  }
}
