package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.PasswordResetToken;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(Users user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 час
        tokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    public Optional<PasswordResetToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(Long id) {
        tokenRepository.deleteById(id);
    }
}
