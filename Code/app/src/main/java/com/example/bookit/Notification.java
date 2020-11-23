package com.example.bookit;

import java.util.ArrayList;

public class Notification {
    private String title;
    private String ISBN;
    private String ownerName;
    private ArrayList<String > requesters;
    private String notificationType;
    private String acceptedUser;


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Notification(String title, String ISBN, String ownerName, ArrayList<String> requesters, String acceptedUser, String notificationType) {
        this.title = title;
        this.ISBN = ISBN;
        this.ownerName = ownerName;
        this.requesters = requesters;
        this.acceptedUser = acceptedUser;
        this.notificationType=notificationType;
    }



    public String getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(String acceptedUser) {
        this.acceptedUser = acceptedUser;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ArrayList<String> getRequesters() {
        return requesters;
    }

    public void setRequesters(ArrayList<String> requesters) {
        this.requesters = requesters;
    }

}
