package com.example.bookshop.data.dto;

public class BookStatusResponse {
    private Boolean result;

    public BookStatusResponse(Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
