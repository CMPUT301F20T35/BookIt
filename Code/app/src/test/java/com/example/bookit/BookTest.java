package com.example.bookit;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookTest {

    // mock Book object
    private Book mockBook() {
        Book book = new Book(null, null, null, null, null,
                null);
        return book;
    }

    // Tests for functionality of Book class
    @Test
    public void testGetTitle() {
        Book book = this.mockBook();
        assertNull(book.getTitle());
    }

    @Test
    public void testSetTitle() {
        Book book = this.mockBook();
        assertNull(book.getTitle());
        book.setTitle("testTitle");
        assertEquals("testTitle", book.getTitle());
    }

    @Test
    public void testGetAuthor() {
        Book book = this.mockBook();
        assertNull(book.getAuthor());
    }

    @Test
    public void testSetAuthor() {
        Book book = this.mockBook();
        assertNull(book.getAuthor());
        book.setAuthor("testAuthor");
        assertEquals("testAuthor", book.getAuthor());
    }

    @Test
    public void testGetISBN() {
        Book book = this.mockBook();
        assertNull(book.getISBN());
    }

    @Test
    public void testSetISBN() {
        Book book = this.mockBook();
        assertNull(book.getISBN());
        book.setISBN("testISBN");
        assertEquals("testISBN", book.getISBN());
    }

    @Test
    public void testGetDescription() {
        Book book = this.mockBook();
        assertNull(book.getDescription());
    }

    @Test
    public void testSetDescription() {
        Book book = this.mockBook();
        assertNull(book.getDescription());
        book.setDescription("testDescription");
        assertEquals("testDescription", book.getDescription());
    }

    @Test
    public void testGetOwnerName() {
        Book book = this.mockBook();
        assertNull(book.getOwnerName());
    }

    @Test
    public void testSetOwnerName() {
        Book book = this.mockBook();
        assertNull(book.getOwnerName());
        book.setOwnerName("testOwnerName");
        assertEquals("testOwnerName", book.getOwnerName());
    }

    @Test
    public void testGetRequestHandler() {
        Book book = this.mockBook();
        assertNull(book.getRequests());
    }

    @Test
    public void testSetRequestHandler() {
        Book book = this.mockBook();
        assertNull(book.getOwnerName());
        RequestHandler requestHandler = new RequestHandler(null, null, null);
        book.setRequests(requestHandler);
        assertEquals(requestHandler, book.getRequests());
    }
}
