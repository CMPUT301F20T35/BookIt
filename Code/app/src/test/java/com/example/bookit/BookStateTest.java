package com.example.bookit;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookStateTest {

    // mock BookState object
    private BookState mockBookState() {
        BookState bookState = new BookState(null, null, null);
        return bookState;
    }

    // Tests for functionality of BookState object
    @Test
    public void testGetBookStatus() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getBookStatus());
    }

    @Test
    public void testSetTitle() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getBookStatus());
        bookState.setBookStatus("testBookStatus");
        assertEquals("testBookStatus", bookState.getBookStatus());
    }

    @Test
    public void testGetLocation() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getLocation());
    }

    @Test
    public void testSetLocation() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getLocation());
        GeoPoint location = new GeoPoint(0.00,0.00);
        bookState.setLocation(location);
        assertEquals(location, bookState.getLocation());
    }

    @Test
    public void testGetHandOffState() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getHandOffState());
    }

    @Test
    public void testSetHandOffState() {
        BookState bookState = this.mockBookState();
        assertNull(bookState.getHandOffState());
        bookState.setHandOffState("testHandOffState");
        assertEquals("testHandOffState", bookState.getHandOffState());
    }
}
