package com.example.bookshop.data.repository;

import com.example.bookshop.data.dto.RatingView;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(value = """
    select bookId,\s
    SUM(CASE WHEN rating = 1 THEN grades_count ELSE 0 END) AS oneCount,
    SUM(CASE WHEN rating = 2 THEN grades_count ELSE 0 END) AS twoCount,
    SUM(CASE WHEN rating = 3 THEN grades_count ELSE 0 END) AS threeCount,
    SUM(CASE WHEN rating = 4 THEN grades_count ELSE 0 END) AS fourCount,
    SUM(CASE WHEN rating = 5 THEN grades_count ELSE 0 END) AS fiveCount
    FROM
    (select book_id as bookId, rating, COUNT(user_id) grades_count from book_ratings where book_id=?1 group by rating, book_id) t1
    GROUP BY bookId
    """, nativeQuery = true)
    RatingView getBookRatings(Integer bookId);
}
