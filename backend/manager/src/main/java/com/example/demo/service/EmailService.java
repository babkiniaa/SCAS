package com.example.demo.service;

import com.example.demo.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String verificationCode, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        String subject = "Please verify your registration";
        String content = "Dear user,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SCASIK_STASIK.";
        sendEmail(email, subject, content, verificationCode, request, "/verify?code=");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendVerificationPassword(String email, String verificationCode, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String subject = "Here's the link to reset your password";
        String content = "Dear user,<br>"
                + "Please follow the link below to change your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SCASIK_STASIK.";
        sendEmail(email, subject, content, verificationCode, request, "/change-password?code=");
    }

    private void sendEmail(String toAddress, String subject, String content, String verificationCode, HttpServletRequest request, String partVerifyURL) throws MessagingException, UnsupportedEncodingException {
        String URL = getSiteURL(request);
        String fromAddress = "scasproject66@gmail.com";
        String senderName = "SCAS";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String verifyURL = URL + partVerifyURL + verificationCode;
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
