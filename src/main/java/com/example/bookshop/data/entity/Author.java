package com.example.bookshop.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String photo;
    private String slug;
    private String description;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Book> bookList = new ArrayList<>();

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int descriptionPartIndex() {
        int sentenceIndex = 0;
        int charIndex = 0;
        while(sentenceIndex < 4 || charIndex >= description.length()-1) {
            if(description.charAt(charIndex++) == '.') sentenceIndex++;
        }
        return charIndex+1;
    }

    public String getVisibleDescriptionPart() {
        return description.substring(0, descriptionPartIndex()).trim();
    }

    public String getHiddenDescriptionPart() {
        if(descriptionPartIndex() >= description.length()-1) return null;
        return description.substring(descriptionPartIndex(), description.length()).trim();
    }
}
