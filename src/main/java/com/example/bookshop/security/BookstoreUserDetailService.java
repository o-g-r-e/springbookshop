package com.example.bookshop.security;

import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(userEmail);
        if(bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("user not found!");
        }
    }
}
