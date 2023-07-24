package com.example.bookshop.controllers;

import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.Author;
import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.service.AuthorService;
import com.example.bookshop.data.service.BookService;
import com.example.bookshop.security.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class AuthorsController {

    private static final Pattern authorSecondNamePattern = Pattern.compile("\\s[^\s]+$");

    private final AuthorService authorService;
    private final BookService bookService;

    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthorsController(AuthorService dataService, BookService bookService, BookstoreUserRegister userRegister) {
        this.authorService = dataService;
        this.bookService = bookService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("currentUser")
    public BookstoreUser currentUser() {
        return userRegister.getCurrentUser();
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        System.out.println("Call /authors");
        List<Author> authors = authorService.getAuthors();
        Map<String, List<Author>> authorsDic = authors.stream().collect(Collectors.groupingBy(a -> {
            char letter = a.getName().charAt(0);
            Matcher secondNameMatcher = authorSecondNamePattern.matcher(a.getName());
            if(secondNameMatcher.find()) {
                letter = secondNameMatcher.group().trim().charAt(0);
            }
            return String.valueOf(letter).toUpperCase();
        }));
        model.addAttribute("authorsDic", authorsDic);
        return "authors/index";
    }

    @GetMapping("/authors/{authorSlug}")
    public String authors(Model model, @PathVariable String authorSlug) {
        model.addAttribute("author", authorService.getAuthorBuSlug(authorSlug));
        model.addAttribute("authorBooksList", bookService.getBooksByAuthor(authorSlug, 0, 6));
        return "authors/slug";
    }
}
