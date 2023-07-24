package com.example.bookshop.data.dto;

public class BookRatingDto {
    private Integer bookRating = 0;
    private RatingView ratingView;
    private Integer ratingsCount = 0;

    public BookRatingDto(RatingView ratingView) {
        this.ratingView = ratingView;



        if(ratingView != null) {
            int ratingSum = ratingView.getOneCount() * 1 +
                    ratingView.getTwoCount() * 2 +
                    ratingView.getThreeCount() * 3 +
                    ratingView.getFourCount() * 4 +
                    ratingView.getFiveCount() * 5;

            this.ratingsCount = ratingView.getOneCount() +
                    ratingView.getTwoCount() +
                    ratingView.getThreeCount() +
                    ratingView.getFourCount() +
                    ratingView.getFiveCount();

            this.bookRating = Math.round(ratingSum / this.ratingsCount);
        }
    }

    public Integer getBookRating() {
        return bookRating;
    }

    public void setBookRating(Integer bookRating) {
        this.bookRating = bookRating;
    }

    public RatingView getRatingView() {
        return ratingView;
    }

    public void setRatingView(RatingView ratingView) {
        this.ratingView = ratingView;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }
}
