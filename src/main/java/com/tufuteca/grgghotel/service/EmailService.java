package com.tufuteca.grgghotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetToken(String to, String token) {
        String url = "http://176.197.44.250:8080/change-password?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject("Запрос на смену пароля");
        email.setText("Что бы сменить пароль нажмите:\n" + url);
        mailSender.send(email);
    }
}
