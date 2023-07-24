package com.example.bookshop.data.service;

import com.example.bookshop.data.dto.RatingView;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public RatingView getBookRatings(Integer bookId) {
        return ratingRepository.getBookRatings(bookId);
    }
}
