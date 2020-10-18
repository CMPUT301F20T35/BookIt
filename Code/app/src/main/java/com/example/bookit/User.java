package com.example.bookit;

import android.media.Image;
import android.widget.ImageView;

public class User {
    private String name;
    private String userName;
    private String userID;
    private String email;
    private int contactInfo;
    private Image image;
    private String password;

    public User(String name, String userName, String userID, String email, int contactInfo, String password) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.email = email;
        this.contactInfo = contactInfo;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(int contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
