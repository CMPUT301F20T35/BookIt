package com.example.bookit;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

public class BookState {
    private String bookStatus;
    private GeoPoint location;
    private String handOffState;

    /**
     * This constructor takes no parameters
     */
    public BookState() {
        this("AVAILABLE",new GeoPoint(0,0),null);
    }

    /**
     * This constructor takes in three parameters
     * @param bookStatus "Accepted", "Available", "Borrowed" or "Requested"
     * @param handOffState
     * @param geoPoint location of the book hand off
     */
    public BookState(String bookStatus, @Nullable GeoPoint geoPoint, @Nullable String handOffState) {
        this.bookStatus = bookStatus;
        this.location = geoPoint;
        this.handOffState = handOffState;
    }

    /**
     * getter of bookStatus
     * @return bookStatus of the book
     */
    public String getBookStatus() {
        return bookStatus;
    }

    /**
     * setter of BookStatus
     * @param bookStatus bookStatue intended to be set for this BookState
     */
    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    /**
     * getter of location
     * @return location of this BookState
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * setter of location
     * @param geoPoint location intended to be set for this BookState
     */
    public void setLocation(GeoPoint geoPoint) {
        this.location = geoPoint;
    }

    /**
     * getter of handOffState
     * @return handOffState of this BookState
     */
    public String getHandOffState() {
        return handOffState;
    }

    /**
     * setter of handOffState
     * @param handOffState handOffState intended to be set for this BookState
     */
    public void setHandOffState(String handOffState) {
        this.handOffState = handOffState;
    }


}
