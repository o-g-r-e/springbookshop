package com.example.bookshop.data;

import org.springframework.beans.factory.annotation.Value;

public class CountedTag {
    private String tagValue;
    private Long booksCount;

    public CountedTag(String tagValue, Long booksCount) {
        this.tagValue = tagValue;
        this.booksCount = booksCount;
    }

    public String sizeClass() {
        if(booksCount >= 20) {
            return "Tag_lg";
        } else if(booksCount >= 16 && booksCount < 20) {
            return "Tag_md";
        } else if(booksCount >= 12 && booksCount < 16) {
            return "";
        } else if(booksCount >= 8 && booksCount < 12) {
            return "Tag_sm";
        }
        return "Tag_xs";
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Long getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(Long booksCount) {
        this.booksCount = booksCount;
    }
}
