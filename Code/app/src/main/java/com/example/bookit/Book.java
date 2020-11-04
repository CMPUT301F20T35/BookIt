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


    /**
     * This constructor takes in six parameters
     * @param title title of the book
     * @param author author of the book
     * @param ISBN ISBN of the book
     * @param description description of the book
     * @param ownerName name of owner of the book
     * @param requestHandler request handler of the book
     */
    public Book(String title, String author, String ISBN,
                String description, String ownerName, RequestHandler requestHandler) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.ownerName = ownerName;
        this.requests=requestHandler;
    }

    /**
     *
     * @param title
     * @param author
     * @param ISBN
     * @param description
     */
    public Book(String title, String author, String ISBN, String description) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
    }

    /**
     * getter of RequestHandler
     * @return request handler of the book
     */
    public RequestHandler getRequests() {
        return requests;
    }

    /**
     * setter of RequestHandler
     * @param requests request handler intended to be set for the book
     */
    public void setRequests(RequestHandler requests) {
        this.requests = requests;
    }

    /**
     * getter of title
     * @return title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter of title
     * @param title title intended to be set for the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter of author
     * @return author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * setter of author
     * @param author author intended to be set for the book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * getter of ISBN
     * @return ISBN of the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * setter of ISBN
     * @param ISBN ISBN intended to be set for the book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * getter of description
     * @return description of the book
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter of description
     * @param description description intended to be set for the book
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter of ownerName
     * @return owner name of the book
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * setter of ownerName
     * @param ownerName owner name intended to be set for the book
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}