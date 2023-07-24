package com.example.bookshop.data.dto;

import java.time.LocalDateTime;

public interface ReviewView {
    LocalDateTime getReviewTime();
    String getReviewText();
    String getUserName();
    Integer getReviewLikes();
    Integer getReviewDislikes();
    Integer getUserBookRating();

    default int textPartIndex() {
        int sentenceIndex = 0;
        int charIndex = 0;
        while(sentenceIndex < 5 || charIndex >= getReviewText().length()-1) {
            if(getReviewText().charAt(charIndex++) == '.') sentenceIndex++;
        }
        return charIndex+1;
    }

    default String getVisibleReviewText() {
        return getReviewText().substring(0, textPartIndex());
    }

    default String getHiddenReviewText() {
        if(textPartIndex() >= getReviewText().length()-1) return "";
        return getReviewText().substring(textPartIndex(), getReviewText().length());
    }
}
