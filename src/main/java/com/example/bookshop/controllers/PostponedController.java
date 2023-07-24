package com.example.bookshop.controllers;

import com.example.bookshop.data.dto.BookStatusResponse;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.service.BookService;
import com.example.bookshop.security.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/postponed")
public class PostponedController {

    private final BookService bookService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public PostponedController(BookService bookService, BookstoreUserRegister userRegister) {
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

    @GetMapping("")
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents, Model model) {
        if(postponedContents != null && !"".equals(postponedContents)) {
            String[] bookSlugs = postponedContents.split("/");
            model.addAttribute("postponedBooks", bookService.getBooksBySlugIn(bookSlugs));
        }
        return "postponed";
    }

    @PostMapping("/add/{bookSlug}")
    @ResponseBody
    public BookStatusResponse postponedAdd(@PathVariable("bookSlug") String bookSlug, @CookieValue(value = "postponedContents", required = false) String postponedContents, HttpServletResponse response) {

        boolean b = postponedContents != null;

        if(b && postponedContents.contains(bookSlug)) {
            return new BookStatusResponse(true);
        }

        Cookie cookie = new Cookie("postponedContents", (b?"/":"") + bookSlug);
        cookie.setPath("/postponed");
        response.addCookie(cookie);
        return new BookStatusResponse(true);
    }

    @PostMapping("/remove/{bookSlug}")
    @ResponseBody
    public BookStatusResponse postponedRemove(@PathVariable("bookSlug") String bookSlug, @CookieValue(value = "postponedContents", required = false) String postponedContents, HttpServletResponse response) {

        if(postponedContents == null || "".equals(postponedContents) || !postponedContents.contains(bookSlug)) return new BookStatusResponse(true);

        ArrayList<String> bookSlugs = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
        bookSlugs.remove(bookSlug);

        Cookie cookie = new Cookie("postponedContents", String.join("/", bookSlugs));
        cookie.setPath("/postponed");
        response.addCookie(cookie);
        return new BookStatusResponse(true);
    }
}
