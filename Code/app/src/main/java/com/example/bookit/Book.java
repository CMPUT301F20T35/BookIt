package com.example.bookit;

import android.widget.ImageView;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String ownerName;
    private RequestHandler requests;
//missing image list


    public Book(String title, String author, String ISBN,
                String description, String ownerName, RequestHandler requestHandler) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.ownerName = ownerName;
        this.requests=requestHandler;
    }
    public RequestHandler getRequests() {
        return requests;
    }

    public void setRequests(RequestHandler requests) {
        this.requests = requests;
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