package com.example.bookit;

import androidx.annotation.Nullable;

public class State {
    private String bookStatus;
    private Location location;
    private String handOffState;

    public State() {
        this("AVAILABLE",null,null);
    }

    /**
     * This constructor takes in three parameters
     * @param bookStatus
     * @param handOffState
     * @param location
     */
    public State(String bookStatus, @Nullable Location location, @Nullable String handOffState) {
        this.bookStatus = bookStatus;
        this.location = location;
        this.handOffState = handOffState;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getHandOffState() {
        return handOffState;
    }

    public void setHandOffState(String handOffState) {
        this.handOffState = handOffState;
    }


}
