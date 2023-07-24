package com.example.bookshop.data.service;

import com.example.bookshop.data.entity.Author;
import com.example.bookshop.data.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorBuSlug(String authorSlug) {
        return authorRepository.findFirstBySlug(authorSlug);
    }

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }
}
