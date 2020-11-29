package com.example.bookit;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationTest {

    // mock Location object
    private Location mockLocation(){
        Location location= new Location(null, null);
        return location;
    }

    // Tests for functionality of Location class
    @Test
    public void testGetLongitude() {
        Location location = this.mockLocation();
        assertNull(location.getLongitude());
    }

    @Test
    public void testSetLongitude() {
        Location location = this.mockLocation();
        assertNull(location.getLongitude());
        location.setLongitude(0.00);
        assertEquals(0.00, (double) location.getLongitude());
    }

    @Test
    public void testGetLatitude() {
        Location location = this.mockLocation();
        assertNull(location.getLatitude());
    }

    @Test
    public void testSetLatitude() {
        Location location = this.mockLocation();
        assertNull(location.getLatitude());
        location.setLatitude(0.00);
        assertEquals(0.00, (double) location.getLatitude());
    }
}
