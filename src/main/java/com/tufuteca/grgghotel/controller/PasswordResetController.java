package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.PasswordResetToken;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.service.EmailService;
import com.tufuteca.grgghotel.service.PasswordResetTokenService;
import com.tufuteca.grgghotel.service.PhoneService;
import com.tufuteca.grgghotel.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneService phoneService;


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("contactMethod") String contactMethod,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "phone", required = false) String phone) {
        if ("email".equals(contactMethod)) {
            // Обработка восстановления пароля по электронной почте
            if (email == null || email.isEmpty()) {
                return "redirect:/login?missingEmail";
            }
            Optional<Users> user = userService.findByEmail(email);
            if (user.isEmpty()) {
                return "redirect:/login?invalidMail";
            }

            PasswordResetToken token = tokenService.createToken(user.orElse(null));
            emailService.sendPasswordResetToken(user.get().getEmail(), token.getToken());
        } else if ("phone".equals(contactMethod)) {
            // Обработка восстановления пароля по телефону
            if (phone == null || phone.isEmpty()) {
                return "redirect:/login?missingPhone";
            }
            Optional<Users> user = userService.findByPhoneNumber(phone);
            if (user.isEmpty()) {
                return "redirect:/login?missingPhone";
            }

            PasswordResetToken token = tokenService.createToken(user.orElse(null));
            phoneService.sendPasswordResetToken(user.get().getPhoneNumber(), token.getToken());
            return "redirect:/login?successPhone";
        } else {
            return "redirect:/login?invalidContactMethod";
        }

        return "redirect:/login?success";
    }



    @GetMapping("/reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenService.getToken(token).orElse(null);
        if (resetToken == null) {
            model.addAttribute("error", "Invalid or expired token");
            return "error";
        }
        model.addAttribute("token", token);
        return "change-password";
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int USERNAME_LENGTH = 8;

    @PostMapping("/change-password")
    public String savePassword(@RequestParam("token") String token,
                               @RequestParam("password") String password) {
        PasswordResetToken resetToken = tokenService.getToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        Users user = resetToken.getUser();
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            user.setUsername(generateRandomUsername());
        }
        userService.changeUserPassword(user, password);
        tokenService.deleteToken(resetToken.getId());
        return "redirect:/login?passwordChanged";
    }

    public static String generateRandomUsername() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}

