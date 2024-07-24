package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.service.BookingService;
import com.tufuteca.grgghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/user-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getUserProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (model.containsAttribute("successBooking")) {
            model.addAttribute("successMessage", "Бронирование успешно!");
        }
        if (model.containsAttribute("errorBooking")) {
            model.addAttribute("errorMessage", "Нет доступных комнат в указанные даты.");
        }

        Users user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        List<Booking> bookings = bookingService.findByUsers(user);
        model.addAttribute("bookings", bookings);
        return "user-profile";
    }

    @PostMapping("/user-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String updateUserProfile(@ModelAttribute("user") Users updatedUser, Model model) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = userService.findByUsername(authentication.getName());

        // Обновляем данные пользователя
        currentUser.setSurname(updatedUser.getSurname());
        currentUser.setName(updatedUser.getName());
        currentUser.setPatronymic(updatedUser.getPatronymic());
        currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        currentUser.setEmail(updatedUser.getEmail());

        // Сохраняем изменения
        userService.addUser(currentUser);

        // Обновляем модель
        model.addAttribute("user", currentUser);
        model.addAttribute("successMessage", "Данные успешно обновлены!");
        List<Booking> bookings = bookingService.findByUsers(currentUser);
        model.addAttribute("bookings", bookings);
        return "user-profile";
    }
    @PostMapping("/edit-password")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String editPassword(@RequestParam("oldPassword") String oldPassword,
                               @RequestParam("newPassword") String newPassword,
                               @RequestParam("checkPassword") String checkPassword, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = userService.findByUsername(authentication.getName());
        model.addAttribute("user", currentUser);
        List<Booking> bookings = bookingService.findByUsers(currentUser);
        model.addAttribute("bookings", bookings);
        if(newPassword.equals(checkPassword)){
            if(new BCryptPasswordEncoder().matches(oldPassword,currentUser.getPassword())){
                currentUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                userService.addUser(currentUser);
                model.addAttribute("successMessage", "Пароль успешно обновлен!");
                return "user-profile";
            }else{
                model.addAttribute("successMessage", "Пароль введен неверно!");
                return "user-profile";
            }
        }else{
            model.addAttribute("successMessage", "Пароли различаются!");
            return "user-profile";
        }
    }
    @PostMapping("/delete-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = userService.findByUsername(authentication.getName());
        currentUser.setDeleted(true);
        userService.addUser(currentUser);
        model.addAttribute("profileDeleted", "Профиль удален!");
        authentication.setAuthenticated(false);
        return "user-profile";
    }




}
