package com.example.bookshop.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "book2tag")
public class Book2Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "book_id")
    @JsonIgnore
    private Integer bookId;

    @Column(name = "tag_id")
    @JsonIgnore
    private Integer tagId;
}