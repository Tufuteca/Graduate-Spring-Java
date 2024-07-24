package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UsersRepository userRepository;
    private final UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository userRepository, UsersRepository usersRepository) {
        this.userRepository = userRepository;
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return (List<Users>) userRepository.findAll();
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean checkPassword(Users user, String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, user.getPassword());
    }

    public void addUser(Users newUser) {
        userRepository.save(newUser);
    }

    private Map<String, String> resetCodes = new HashMap<>();

    public Users findByIdentifier(String identifier) {
        Optional<Users> user = userRepository.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userRepository.findByUsername(identifier);
        }
        if (user.isEmpty()) {
            user = userRepository.findByPhoneNumber(identifier);
        }
        return user.orElse(null);
    }

    public void changeUserPassword(Users users, String password) {
        users.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(users);
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Users> findByPhoneNumber(String phone) {
        return usersRepository.findByPhoneNumber(phone);
    }
}

