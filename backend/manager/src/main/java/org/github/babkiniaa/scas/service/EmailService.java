package org.github.babkiniaa.scas.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки электронных писем, включая верификацию регистрации и сброс пароля.
 * Использует {@link JavaMailSender} для отправки писем пользователям.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Отправляет электронное письмо для верификации регистрации пользователя.
     *
     * @param email          адрес электронной почты получателя
     * @param verificationCode код верификации
     * @param request        объект {@link HttpServletRequest}, необходимый для получения URL сайта
     * @throws UnsupportedEncodingException если кодировка не поддерживается
     * @throws MessagingException если произошла ошибка при отправке сообщения
     */
    public void sendVerificationEmail(
            String email, String verificationCode, HttpServletRequest request
    ) throws UnsupportedEncodingException, MessagingException {
        String subject = "Please verify your registration";
        String content = "Dear user,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SCASIK_STASIK.";
        sendEmail(email, subject, content, verificationCode, request, "/auth/verify?code=");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    /**
     * Отправляет электронное письмо для сброса пароля пользователя.
     *
     * @param email          адрес электронной почты получателя
     * @param verificationCode код верификации
     * @param request        объект {@link HttpServletRequest}, необходимый для получения URL сайта
     * @throws MessagingException если произошла ошибка при отправке сообщения
     * @throws UnsupportedEncodingException если кодировка не поддерживается
     */
    public void sendVerificationPassword(
            String email, String verificationCode, HttpServletRequest request
    ) throws MessagingException, UnsupportedEncodingException {
        String subject = "Here's the link to reset your password";
        String content = "Dear user,<br>"
                + "Please follow the link below to change your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SCASIK_STASIK.";
        sendEmail(email,
                subject,
                content,
                verificationCode,
                request,
                "/password/change-password?code=");
    }

    private void sendEmail(
            String toAddress,
            String subject,
            String content,
            String verificationCode,
            HttpServletRequest request,
            String partVerifyURL
    ) throws MessagingException, UnsupportedEncodingException {
        String Url = getSiteURL(request);
        String fromAddress = "scasproject66@gmail.com";
        String senderName = "SCAS";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String verifyURL = Url + partVerifyURL + verificationCode;
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
