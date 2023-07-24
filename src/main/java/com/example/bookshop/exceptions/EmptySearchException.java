package com.example.bookshop.exceptions;

public class EmptySearchException extends Throwable {
    public EmptySearchException(String message) {
        super(message);
    }
}
