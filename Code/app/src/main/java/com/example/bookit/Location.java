package com.example.bookit;

//used to store and retrieve the location selected by the user.
public class Location {
    private Double longitude;
    private Double latitude;
    
    /**
     * This constructor takes in two parameters
     * @param longitude longitude of the location
     * @param latitude latitude of the location
     */
    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * getter of longitude
     * @return longitude of the location
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * setter of longitude
     * @param longitude longitude intended to be set for the location
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * getter of latitude
     * @return latitude of the location
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * setter of latitude
     * @param latitude latitude intended to be set for the location
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
