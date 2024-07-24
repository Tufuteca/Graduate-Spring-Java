package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.users.Reviews;
import com.tufuteca.grgghotel.repository.users.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewsService {
    @Autowired
    private ReviewsRepository reviewsRepository;

    public List<Reviews> getAllReviews() {
        return reviewsRepository.findAll();
    }

    public void save(Reviews review) {
        reviewsRepository.save(review);
    }

    public void deleteReviewById(Long reviewId) {
        reviewsRepository.deleteById(reviewId);
    }

    public List<Reviews> getAllReviewsOrderByCommentDateDesc() {
        return reviewsRepository.findAllByOrderByCommentDateDesc();
    }

    public Reviews findById(Long reviewId) {
        return reviewsRepository.findReviewById(reviewId);
    }
}
