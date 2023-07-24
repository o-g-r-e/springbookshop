package com.example.bookshop.data.dto;


import com.example.bookshop.data.entity.Book;

import java.util.List;

public class CartBooksDto {
    private List<Book> books;
    private Double totalPrice;
    private Double totalDiscountPrice;

    public CartBooksDto(List<Book> books) {
        this.books = books;
        this.totalPrice = 0.0;
        this.totalDiscountPrice = 0.0;
        for(Book b : books) {
            if(b.getDiscount() > 0) {
                totalPrice += b.getPrice()/100;
                totalDiscountPrice += b.discountPrice();
            }
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public void setTotalDiscountPrice(Double totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }
}
