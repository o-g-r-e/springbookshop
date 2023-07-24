package com.example.bookshop.data.service;

import com.example.bookshop.data.dto.RatingView;
import com.example.bookshop.data.dto.ReviewView;
import com.example.bookshop.data.repository.RatingRepository;
import com.example.bookshop.data.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewView> getReviews(Integer bookId) {
        return reviewRepository.getReviewsByBookId(bookId);
    }
}
