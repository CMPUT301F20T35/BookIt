package com.example.bookit;

import android.media.Image;
import android.widget.ImageView;

public class User {
    private String name;
    private String userName;
    private String userID;
    private String email;
    private String contactInfo;
    private Image image;
    private String password;

    /**
     * This constructor takes in six parameters
     * @param name name of user
     * @param userName user name of user
     * @param userID user ID of user
     * @param email email of user
     * @param contactInfo contact Info of user
     * @param password password of user
     */
    public User(String name, String userName, String userID, String email, String contactInfo, String password) {
        this.name = name;
        this.userName = userName;
        this.userID = userID;
        this.email = email;
        this.contactInfo = contactInfo;
        this.password = password;
    }

    /**
     * getter of name
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * setter of name
     * @param name name intended to be set for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of user name
     * @return user name of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setter of user name
     * @param userName user name intended to be set for the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getter of user ID
     * @return user ID for the user
     */
    public String getUserID() {
        return userID;
    }

    /**
     * setter of user ID
     * @param userID user ID intended to be set for the user
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * getter of email
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter of email
     * @param email email intended to be set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter of contact Info
     * @return contact Info of user
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * setter of contact Info
     * @param contactInfo contact Info intended to be set for the user
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * getter of image
     * @return image of the user
     */
    public Image getImage() {
        return image;
    }

    /**
     * setter of image
     * @param image image intended to be set for the user
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * getter of password
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter of password
     * @param password password intended to be set for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
