package com.example.bookshop.data.repository;

import com.example.bookshop.data.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findFirstBySlug(String slug);
}
