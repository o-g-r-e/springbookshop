package com.example.bookshop.data.repository;

import com.example.bookshop.data.dto.ReviewView;
import com.example.bookshop.data.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = """
    SELECT t2.review_time reviewTime, t2.review_text reviewText, t2.user_name userName, t2.rating userBookRating,
    SUM(CASE WHEN value = 1 THEN value ELSE 0 END) AS reviewLikes,
    SUM(CASE WHEN value = -1 THEN ABS(value) ELSE 0 END) AS reviewDislikes
    FROM book_review_like
    JOIN (SELECT
            t1.review_id,
        book_ratings.rating,
        t1.user_name,
        t1.review_time,
        t1.review_text
        FROM
        (select book_review.id review_id, book_review.book_id, users.name user_name, users.id user_id, time review_time, text review_text from book_review\s
            JOIN users
            ON users.id = user_id
            where book_review.book_id=?1) t1
            LEFT JOIN book_ratings
            ON book_ratings.user_id = t1.user_id AND book_ratings.book_id = t1.book_id) AS t2
    ON t2.review_id = book_review_like.review_id
    GROUP BY t2.review_time, t2.review_text, t2.user_name, t2.rating
    """, nativeQuery = true)
    List<ReviewView> getReviewsByBookId(Integer bookId);
}
