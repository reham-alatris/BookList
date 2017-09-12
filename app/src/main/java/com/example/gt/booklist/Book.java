package com.example.gt.booklist;

/**
 * Created by g.t on 15/06/2017.
 */

public class Book {
    private String title;
    private String author;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, String author) {

        this.title = title;
        this.author = author;
    }
}