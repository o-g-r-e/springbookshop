package com.example.bookshop.controllers;

import com.example.bookshop.data.ResourceStorage;
import com.example.bookshop.data.dto.BookRatingDto;
import com.example.bookshop.data.dto.SearchWordDto;
import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.service.*;
import com.example.bookshop.data.entity.Book;
import com.example.bookshop.data.dto.BooksPageDto;
import com.example.bookshop.security.BookstoreUserRegister;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final TagService tagService;
    private final ResourceStorage resourceStorage;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final BookstoreUserRegister userRegister;

    public BooksController(BookService bookService, AuthorService authorService, TagService tagService, ResourceStorage resourceStorage, RatingService ratingService, ReviewService reviewService, BookstoreUserRegister userRegister) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.resourceStorage = resourceStorage;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
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

    @GetMapping("/recent")
    public String recentPage(Model model) {
        model.addAttribute("recentBooksList", bookService.getRecentBooks(10));
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularPage(Model model) {
        List<Book> popularBooks = bookService.getPopularBooks(0, 10);
        model.addAttribute("popularBooksList", popularBooks);
        return "books/popular";
    }

    @GetMapping("/author/{authorSlug}")
    public String authorBooks(Model model, @PathVariable String authorSlug) {
        model.addAttribute("authorBooksList", bookService.getBooksByAuthor(authorSlug, 0, 10));
        model.addAttribute("author", authorService.getAuthorBuSlug(authorSlug));
        return "/books/author";
    }

    @GetMapping("/{bookSlug}")
    public String book(Model model, @PathVariable("bookSlug") String bookSlug) {
        Book slugBook = bookService.getBookBySlug(bookSlug);
        model.addAttribute("book", slugBook);
        model.addAttribute("bookTags", tagService.getTagsByBookId(slugBook.getId()));
        model.addAttribute("rating", new BookRatingDto( ratingService.getBookRatings(slugBook.getId()) ) );
        model.addAttribute("reviews", reviewService.getReviews(slugBook.getId()));

        if(userRegister.getCurrentUser() != null) {
            return "books/slugmy";
        }

        return "books/slug";
    }

    @PostMapping("/{bookSlug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("bookSlug") String bookSlug) throws IOException {
        //System.out.println("BOOK SLUG "+bookSlug);
        String savePath = resourceStorage.saveNewBookImage(file, bookSlug);
        Book bookToUpdate = bookService.getBookBySlug(bookSlug);
        bookToUpdate.setImage(savePath);
        bookService.saveBook(bookToUpdate); //save new path in db here

        return ("redirect:/books/" + bookSlug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash")String hash) throws IOException{
        Path path = resourceStorage.getBookFilePath(hash);

        MediaType mediaType = resourceStorage.getBookFileMime(hash);

        byte[] data = resourceStorage.getBookFileByteArray(hash);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/recentpage")
    @ResponseBody
    public BooksPageDto recentPage(@RequestParam("from") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate from, @RequestParam("to") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate to, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksWithPubDateBetween(from, to, offset, limit));
    }

    @GetMapping("/popularpage")
    @ResponseBody
    public BooksPageDto popularBooks(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPopularBooks(offset, limit));
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto recommendedBooks(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/taggedbookspage")
    @ResponseBody
    public BooksPageDto taggedBooks(@RequestParam("tag") String tagValue, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getBooksByTag(tagValue, offset, limit));
    }

    @GetMapping("/genresbookspage")
    @ResponseBody
    public BooksPageDto genresPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, @RequestParam("genreSlug") String genreSlug) {
        return new BooksPageDto(bookService.getBooksByGenre(genreSlug, offset, limit));
    }

    @GetMapping("/authorbookspage")
    @ResponseBody
    public BooksPageDto authorBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, @RequestParam("authorSlug") String authorSlug) {
        return new BooksPageDto(bookService.getBooksByAuthor(authorSlug, offset, limit));
    }
}
