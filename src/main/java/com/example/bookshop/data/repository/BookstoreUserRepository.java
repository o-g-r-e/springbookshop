package com.example.bookshop.data.repository;

import com.example.bookshop.data.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    BookstoreUser findBookstoreUserByEmail(String email);
}
