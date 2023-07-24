package com.example.bookshop.controllers;

import com.example.bookshop.data.ApiResponse;
import com.example.bookshop.data.entity.Book;
import com.example.bookshop.data.service.BookService;
import com.example.bookshop.exceptions.BookstoreApiWrongParameterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.MissingFormatArgumentException;

@RestController
@RequestMapping("/api")
@Tag(name="", description = "book data api")
public class BooksRestApiController {

    private final BookService dataService;

    @Autowired
    public BooksRestApiController(BookService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/books/by-author")
    @Operation(description = "request to fetch book list by author name")
    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(dataService.getBooksByAuthor(authorName));
    }

    @GetMapping("/books/by-title")
    @Operation(description = "request to fetch book list by book title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String bookTitle) throws BookstoreApiWrongParameterException {
        ApiResponse<Book> response = new ApiResponse<>();
        List<Book> books = dataService.getBooksByTitle(bookTitle);
        response.setDebugMessage("successful request");
        response.setMessage("data size: "+books.size()+" elements");
        response.setStatus(HttpStatus.OK);
        response.setData(books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price-range")
    @Operation(description = "request to fetch book list by price range from min to max price")
    public ResponseEntity<List<Book>> booksByPriceRange(@RequestParam("min") String min, @RequestParam("max") String max) {
        Integer minPrice = Integer.parseInt(min.replace(".", ""));
        Integer maxPrice = Integer.parseInt(max.replace(".", ""));
        return ResponseEntity.ok(dataService.getBooksWithPriceBetween(minPrice, maxPrice));
    }

    @GetMapping("/books/with-price")
    @Operation(description = "request to fetch book list by price")
    public ResponseEntity<List<Book>> booksByPrice(@RequestParam("price") String price) {
        return ResponseEntity.ok(dataService.getBooksWithPrice(Integer.parseInt(price.replace(".", ""))));
    }

    @GetMapping("/books/with-max-discount")
    @Operation(description = "request to fetch book list with max discount")
    public ResponseEntity<List<Book>> booksByMaxDiscount() {
        return ResponseEntity.ok(dataService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/bestsellers")
    @Operation(description = "request to fetch bestsellers (is_bestsellers = 1) book list")
    public ResponseEntity<List<Book>> booksByBestsellers() {
        return ResponseEntity.ok(dataService.getBestsellers());
    }

    @GetMapping("/books/by-pubdate-range")
    public ResponseEntity<List<Book>> booksByPriceRange(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return ResponseEntity.ok(dataService.getBooksWithPubDateBetween(start, end));
    }

    @GetMapping("/books/popular")
    public ResponseEntity<List<Book>> booksByPopularRait(@RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(dataService.getPopularBooks(0, limit));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Bad parameter value...", ex), HttpStatus.BAD_REQUEST);
    }
}
