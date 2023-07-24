package com.example.bookshop.data;

import com.example.bookshop.data.entity.Genre;

import java.util.List;

public class GenreCategory {
    Genre genre;
    List<Genre> subGenres;

    public GenreCategory(Genre genre, List<Genre> subGenres) {
        this.genre = genre;
        this.subGenres = subGenres;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Genre> getSubGenres() {
        return subGenres;
    }

    public void setSubGenres(List<Genre> subGenres) {
        this.subGenres = subGenres;
    }
}