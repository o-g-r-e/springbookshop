package com.example.bookshop.controllers;

import com.example.bookshop.data.CountedTag;
import com.example.bookshop.data.dto.BooksPageDto;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.Book;
import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.service.BookService;
import com.example.bookshop.data.service.GenreService;
import com.example.bookshop.data.service.TagService;
import com.example.bookshop.exceptions.EmptySearchException;
import com.example.bookshop.security.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class PageController {

    private final BookService bookService;
    private final TagService tagService;
    private final GenreService genreService;
    private final BookstoreUserRegister userRegister;

    @Autowired
    public PageController(BookService dataService, TagService tagService, GenreService genreService, BookstoreUserRegister userRegister) {
        this.bookService = dataService;
        this.tagService = tagService;
        this.genreService = genreService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("booksList")
    public List<Book> bookList(){
        List<Book> books = bookService.getPageOfRecommendedBooks(0,6).getContent();
        return books;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("currentUser")
    public BookstoreUser currentUser() {
        return userRegister.getCurrentUser();
    }

    @GetMapping("")
    public String indexPage(Model model) {
        model.addAttribute("popularBooksList", bookService.getPopularBooks(0, 6));
        model.addAttribute("recentBooksList", bookService.getRecentBooks(6));
        List<CountedTag> tags = tagService.getTags();
        model.addAttribute("tagsList", tagService.randomSelectTags(tags));
        return "index";
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("genres", genreService.getSortedGenres());
        return "genres/index";
    }

    @GetMapping("/genres/{genreSlug}")
    public String genresPage(Model model, @PathVariable String genreSlug) {
        model.addAttribute("genresBooks", bookService.getBooksByGenre(genreSlug, 0, 10));
        return "genres/slug";
    }

    @GetMapping("/tags")
    public String taggedBooks(@RequestParam("tag") String tagValue, Model model) {
        model.addAttribute("taggedBooksList", bookService.getBooksByTag(tagValue, 0, 10));
        return "tags/index";
    }

    @GetMapping(value = {"/search/", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto, Model model) throws EmptySearchException {
        if(searchWordDto == null) throw new EmptySearchException("Поиск по null невозможен");
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults", bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
        return "search/index";
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset,limit).getContent());
    }

    @GetMapping("/search")
    public String searchPage() {
        return "111";
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents/index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }
}
