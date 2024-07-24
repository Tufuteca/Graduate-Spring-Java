package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.users.Clients;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.users.UsersRepository;
import com.tufuteca.grgghotel.service.ClientsService;
import com.tufuteca.grgghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class RegistrationController {

        private final UsersRepository usersRepository;
        private final UserService userService;
        private final BCryptPasswordEncoder passwordEncoder;

        @Autowired
        private ClientsService clientsService;

        @Autowired
        public RegistrationController(UsersRepository usersRepository, UserService userService) {
            this.usersRepository = usersRepository;
            this.userService = userService;
            this.passwordEncoder = new BCryptPasswordEncoder();
        }
        @GetMapping("/registrationPage")
        public String getRegistrationPage(Model model){
            return "/registration";
        }


    @PostMapping("/register")
        public String registerUser(@RequestParam("registerUsername") String username,
                                   @RequestParam("registerPassword") String password,
                                   @RequestParam("registerEmail") String email,
                                   @RequestParam("registerPhone") String phoneNumber,
                                   @RequestParam("registerSurname") String surname,
                                   @RequestParam("registerName") String name,
                                   @RequestParam("registerPatronymic") String patronymic,
                                   Model model) {
            // Логика регистрации пользователя
            var newUser = new Users();
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setPatronymic(patronymic);
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setActive(false);
            newUser.setDeleted(false);
            newUser.setBlocked(false);
            Clients clients = new Clients();
            clients.setUser(newUser);
            try {
                userService.addUser(newUser);
                clientsService.addClient(clients);
                return "redirect:/login";
            } catch (DataIntegrityViolationException e) {
                // Обработка ошибки дублирования записи
                String errorMessage = determineErrorMessage(e);
                if(errorMessage.contains("email")){
                    return "redirect:/registration?errorMail";
                } else if (errorMessage.contains("login")) {
                    return "redirect:/registration?errorUsername";
                } else if (errorMessage.contains("phone")) {
                    return "redirect:/registration?errorPhone";
                } else{
                    return "redirect:/registration?error";
                }
            }
        }

        private String determineErrorMessage(DataIntegrityViolationException e) {
            String errorMessage = "Произошла ошибка при регистрации.";

            if (e.getMessage().contains("email_UNIQUE")) {
                errorMessage = "Пользователь с таким email уже зарегистрирован.";
            } else if (e.getMessage().contains("UK_r43af9ap4edm43mmtq01oddj6")) {
                errorMessage = "Пользователь с таким login уже зарегистрирован.";
            } else if (e.getMessage().contains("phone_number_UNIQUE")) {
                errorMessage = "phone";
            }

            return errorMessage;
        }
}
