package com.example.bookshop.data.repository;

import com.example.bookshop.data.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByAuthorNameContaining(String authorName);
    List<Book> findBooksByTitleContaining(String bookTitle);
    List<Book> findBooksByPriceBetween(Integer min, Integer max);
    List<Book> findBooksByPriceIs(Integer price);
    @Query("from Book where isBestSeller > 0")
    List<Book> getBestsellers();
    @Query(value = "SELECT * FROM books WHERE discount=(SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();
    @Query(value = "SELECT * FROM books WHERE pub_date BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Book> booksByPubDateBetween(LocalDate start, LocalDate end);

    List<Book> findBooksByPubDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    @Query(value = """
    -- t1 - выборка отношений пользователь-книга-тип_отношения, исключающий ошибочные дубликаты (для одного пользователя и книги отношение может существовать только в единственном числе)
    -- t2 - количество отношений (total_relations) данного типа (type_id) для каждой книги (book_id)
    -- t3 - транспонированный вид t2 где для каждой книги (book_id) указано количество отношений kept, cart, paid
    -- внешний запрос - добавлен столбец рейтинга популярности и соединение с таблицей books
    SELECT books.id, books.author_id, books.pub_date, books.slug, books.image, books.title, books.description, books.price, books.discount, books.is_bestseller,
    CASE WHEN book_id IS NULL THEN 0 ELSE paid+cart*0.7+kept*0.4 END AS pop_rait
    FROM
        (SELECT book_id,
        SUM(CASE WHEN type_id = 1 THEN total_relations ELSE 0 END) AS kept,
        SUM(CASE WHEN type_id = 2 THEN total_relations ELSE 0 END) AS cart,
        SUM(CASE WHEN type_id = 3 THEN total_relations ELSE 0 END) AS paid
        FROM
            (SELECT book_id, type_id, COUNT(type_id) total_relations
            FROM (SELECT DISTINCT user_id, book_id, type_id FROM book2user WHERE type_id < 4) t1
            GROUP BY book_id, type_id
            ORDER BY book_id) t2
        GROUP BY book_id) t3
    RIGHT JOIN books
    ON book_id=books.id
    ORDER BY pop_rait DESC
    """, nativeQuery = true)
    List<Book> getPopularBooks(Pageable pageable);

    @Query(value = """
    SELECT DISTINCT books.id, books.author_id, books.pub_date, books.slug, books.image, books.title, books.description, books.price, books.discount, books.is_bestseller
    FROM book2tag
    JOIN books
    ON books.id = book_id
    WHERE tag_id IN (SELECT id FROM tags WHERE tag_value = ?1)
    """, nativeQuery = true)
    List<Book> getBooksByTag(String tag, Pageable pageable);

    @Query(value = """
    SELECT DISTINCT books.id, books.author_id, books.pub_date, books.slug, books.image, books.title, books.description, books.price, books.discount, books.is_bestseller
    FROM book2genre
    JOIN books
    ON books.id = book_id
    WHERE genre_id IN (SELECT id FROM genres WHERE slug = ?1)
    """, nativeQuery = true)
    List<Book> getBooksByGenre(String genreSlug, Pageable pageable);

    @Query(value = """
    SELECT books.id, books.author_id, books.pub_date, books.slug, books.image, books.title, books.description, books.price, books.discount, books.is_bestseller
    FROM books
    JOIN (SELECT id FROM authors WHERE slug = ?1 LIMIT 1) t1
    ON t1.id = books.author_id
    """, nativeQuery = true)
    List<Book> getBooksByAuthor(String authorSlug, Pageable pageable);

    Page<Book> findByTitleContaining(String bookTitle, Pageable pageable);

    Book findFirstBySlug(String bookSlug);

    List<Book> findBooksBySlugIn(String[] cookieSlugs);
}
