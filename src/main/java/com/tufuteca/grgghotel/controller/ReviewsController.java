package com.tufuteca.grgghotel.controller;


import com.tufuteca.grgghotel.model.users.Reviews;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.service.BookingService;
import com.tufuteca.grgghotel.service.ReviewsService;
import com.tufuteca.grgghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReviewsController {


    @Autowired
    private ReviewsService reviewsService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @GetMapping("reviews")
    public String getReviews(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findByUsername(authentication.getName());
        List<Reviews> reviews = reviewsService.getAllReviewsOrderByCommentDateDesc(); // Получение отсортированного списка отзывов
        model.addAttribute("reviews", reviews);
        if (model.containsAttribute("successMessage")) {
            model.addAttribute("message", "Отзыв успешно добавлен!");
        }
        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("message", "Ошибка, вы не заполнили поле \"Отзыв\"");
        }
        return "/reviews";
    }

    @PostMapping("reviews")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addReview(@RequestParam("comment_text") String commentText, Authentication authentication, RedirectAttributes redirectAttributes) {
        Users user = userService.findByUsername(authentication.getName());


        if(!commentText.equals("")) {
            // Создание нового объекта отзыва и связывание его с пользователем
            Reviews review = new Reviews();
            review.setComment(commentText);
            review.setCommentDate(LocalDateTime.now());
            review.setUser(user);
            // Сохранение отзыва в базе данных
            reviewsService.save(review);
            redirectAttributes.addFlashAttribute("successMessage","Отзыв успешно добавлен!");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","Ошибка, вы не заполнили поле \"Отзыв\"");
        }

        return "redirect:/reviews"; // Перенаправление на страницу отзывов
    }

    @PostMapping("/deleteReview/{reviewId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewsService.deleteReviewById(reviewId);
        return "redirect:/reviews"; // Перенаправление на страницу отзывов после удаления
    }

    @PostMapping("/deleteReviewByUser/{reviewId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteReviewByUser(@PathVariable("reviewId") Long reviewId) {
        Reviews review = reviewsService.findById(reviewId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findByUsername(authentication.getName());
        if (review != null && review.getUser().getUsername().equals(user.getUsername())) {
            reviewsService.deleteReviewById(reviewId);
        }
        return "redirect:/reviews";
    }


}
