package com.example.bookit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestHandlerTest {

    // mock RequestHandler object
    private RequestHandler mockRequestHandler() {
        RequestHandler requestHandler = new RequestHandler(null, null, null, null, null);
        return requestHandler;
    }

    // Tests for functionality of RequestHandler class
    @Test
    public void testGetState() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getState());
    }

    @Test
    public void testSetState() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getState());
        BookState bookState = new BookState(null, null, null);
        requestHandler.setState(bookState);
        assertEquals(bookState, requestHandler.getState());
    }

    @Test
    public void testGetRequestors() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getRequestors());
    }

    @Test
    public void testSetRequestors() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getRequestors());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test1");
        arrayList.add("test2");
        requestHandler.setRequestors(arrayList);
        assertEquals(arrayList, requestHandler.getRequestors());
    }

    @Test
    public void testGetAcceptedRequestor() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getAcceptedRequestor());
    }

    @Test
    public void testSetAcceptedRequestor() {
        RequestHandler requestHandler = this.mockRequestHandler();
        assertNull(requestHandler.getAcceptedRequestor());
        requestHandler.setAcceptedRequestor("testAcceptedRequestor");
        assertEquals("testAcceptedRequestor", requestHandler.getAcceptedRequestor());
    }
}
