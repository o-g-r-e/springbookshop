package com.example.bookshop.security;

import com.example.bookshop.data.entity.BookstoreUser;
import com.example.bookshop.data.repository.BookstoreUserRepository;
import com.example.bookshop.security.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailService bookstoreUserDetailService;
    private final JWTUtil jwtUtil;

    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailService bookstoreUserDetailService, JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailService = bookstoreUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    public void registerNewUser(RegistrationForm registrationForm) {
        if(bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null) {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setHash(user.toString());
            user.setRegTime(LocalDateTime.now());
            user.setBalance(0);
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            bookstoreUserRepository.save(user);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public BookstoreUser getCurrentUser() {
        if("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) return null;
        BookstoreUserDetails userDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getBookstoreUser();
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }
}
