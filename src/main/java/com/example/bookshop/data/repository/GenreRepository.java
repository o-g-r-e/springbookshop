package com.example.bookshop.data.repository;

import com.example.bookshop.data.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository  extends JpaRepository<Genre, Integer> {
    @Query(value = """
    SELECT genre_id id, genres.name, genres.parent_id, genres.slug, COUNT(book_id) books_count
    FROM book2genre
    JOIN genres
    ON genres.id = genre_id
    GROUP BY genre_id, genres.name, genres.parent_id, genres.slug
    """, nativeQuery = true)
    List<Genre> getGenres();
}
