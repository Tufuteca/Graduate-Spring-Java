package com.tufuteca.grgghotel.repository.users;


import com.tufuteca.grgghotel.model.users.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findAllByOrderByCommentDateDesc();

    Reviews findReviewById(Long reviewId);
}