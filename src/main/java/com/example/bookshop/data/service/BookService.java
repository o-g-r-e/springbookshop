package com.example.bookshop.data.service;

import com.example.bookshop.data.entity.Book;
import com.example.bookshop.data.repository.BookRepository;
import com.example.bookshop.exceptions.BookstoreApiWrongParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }


    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String bookTitle) throws BookstoreApiWrongParameterException {
        if("".equals(bookTitle) || bookTitle.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> books = bookRepository.findBooksByTitleContaining(bookTitle);

            if(books.size() <= 0) throw new BookstoreApiWrongParameterException("No data found with specific parameters...");

            return books;
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceBetween(min, max);
    }

    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceIs(price);
    }

    public List<Book> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public List<Book> getBooksWithPubDateBetween(LocalDate start, LocalDate end) {
        return bookRepository.booksByPubDateBetween(start, end);
    }

    public List<Book> getPageOfBooksWithPubDateBetween(LocalDate start, LocalDate end, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetween(start, end, nextPage);
    }

    public List<Book> getRecentBooks(int limit) {
        LocalDate nowDate = LocalDate.now();
        LocalDate monthsAgoDate = nowDate.minusMonths(3);
        return getPageOfBooksWithPubDateBetween(monthsAgoDate, nowDate, 0, limit);
    }

    public List<Book> getPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getPopularBooks(nextPage);
    }

    public List<Book> getBooksByTag(String tag, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByTag(tag, nextPage);
    }

    public List<Book> getBooksByGenre(String genreSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByGenre(genreSlug, nextPage);
    }

    public List<Book> getBooksByAuthor(String authorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByAuthor(authorSlug, nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findByTitleContaining(searchWord, nextPage);
    }

    public Book getBookBySlug(String bookSlug) {
        return bookRepository.findFirstBySlug(bookSlug);
    }

    public void saveBook(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    public List<Book> getBooksBySlugIn(String[] bookSlugs) {
        return bookRepository.findBooksBySlugIn(bookSlugs);
    }
}
