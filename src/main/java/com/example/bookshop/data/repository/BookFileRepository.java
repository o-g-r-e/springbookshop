package com.example.bookshop.data.repository;

import com.example.bookshop.data.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    public BookFile findBookFileByHash(String hash);
}
