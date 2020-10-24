package com.example.bookit;

import android.widget.ImageView;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private ImageView bookImage;
    private ImageView codeImage;
    private String description;
    private String ownerName;

    public Book(String title, String author, String ISBN, ImageView bookImage, ImageView codeImage,
                String description, String ownerName) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.bookImage = bookImage;
        this.codeImage = codeImage;
        this.description = description;
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public ImageView getBookImage() {
        return bookImage;
    }

    public void setBookImage(ImageView bookImage) {
        this.bookImage = bookImage;
    }

    public ImageView getCodeImage() {
        return codeImage;
    }

    public void setCodeImage(ImageView codeImage) {
        this.codeImage = codeImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}