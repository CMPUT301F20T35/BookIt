package com.example.bookit;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

public class BookState {
    private String bookStatus;
    private GeoPoint location;
    private String handOffState;

    public BookState() {
        this("AVAILABLE",new GeoPoint(0,0),null);
    }


    /**
     * This constructor takes in three parameters
     * @param bookStatus
     * @param handOffState
     * @param geoPoint
     */
    public BookState(String bookStatus, @Nullable GeoPoint geoPoint, @Nullable String handOffState) {
        this.bookStatus = bookStatus;
        this.location = geoPoint;
        this.handOffState = handOffState;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint geoPoint) {
        this.location = geoPoint;
    }


    public String getHandOffState() {
        return handOffState;
    }

    public void setHandOffState(String handOffState) {
        this.handOffState = handOffState;
    }


}
