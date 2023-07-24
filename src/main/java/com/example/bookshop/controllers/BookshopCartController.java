package com.example.bookshop.controllers;

import com.example.bookshop.data.dto.BookStatusResponse;
import com.example.bookshop.data.dto.CartBooksDto;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.Book;
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
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/cart")
public class BookshopCartController {

    private final BookstoreUserRegister userRegister;

    private final BookService bookService;

    @Autowired
    public BookshopCartController(BookstoreUserRegister userRegister, BookService bookService) {
        this.userRegister = userRegister;
        this.bookService = bookService;
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
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) :
                    cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> booksFromCookieSlugs = bookService.getBooksBySlugIn(cookieSlugs);
            CartBooksDto cartBooksDto = new CartBooksDto(booksFromCookieSlugs);
            model.addAttribute("booksCart", cartBooksDto);
        }

        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    @ResponseBody
    public BookStatusResponse handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name =
            "cartContents", required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }

        return new BookStatusResponse(true);
    }

    @PostMapping("/changeBookStatus/{slug}")
    @ResponseBody
    public BookStatusResponse changeBookStatus(@PathVariable("slug") String slug,
                                   @CookieValue(name = "cartContents", required = false) String cartContents,
                                   HttpServletResponse response, Model model) {

        if(cartContents == null || "".equals(cartContents)) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if(!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }
        return new BookStatusResponse(true);
    }
}
