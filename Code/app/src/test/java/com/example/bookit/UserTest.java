package com.example.bookit;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserTest {

    // mock User object
    private User mockUser() {
        User user = new User(null,null,null,null,null,null);
        return user;
    }

    // Tests for functionality of User class
    @Test
    public void testGetName() {
        User user = this.mockUser();
        assertNull(user.getName());
    }

    @Test
    public void testSetName() {
        User user = this.mockUser();
        assertNull(user.getName());
        user.setName("testName");
        assertEquals("testName", user.getName());
    }

    @Test
    public void testGetUserName() {
        User user = this.mockUser();
        assertNull(user.getUserName());
    }

    @Test
    public void testSetUserName() {
        User user = this.mockUser();
        assertNull(user.getUserName());
        user.setUserName("testUserName");
        assertEquals("testUserName", user.getUserName());
    }

    @Test
    public void testGetUserID() {
        User user = this.mockUser();
        assertNull(user.getUserID());
    }

    @Test
    public void testSetUserID() {
        User user = this.mockUser();
        assertNull(user.getUserID());
        user.setUserID("testUserID");
        assertEquals("testUserID", user.getUserID());
    }

    @Test
    public void testGetEmail() {
        User user = this.mockUser();
        assertNull(user.getEmail());
    }

    @Test
    public void testSetEmail() {
        User user = this.mockUser();
        assertNull(user.getEmail());
        user.setEmail("testEmail");
        assertEquals("testEmail", user.getEmail());
    }

    @Test
    public void testGetContactInfo() {
        User user = this.mockUser();
        assertNull(user.getContactInfo());
    }

    @Test
    public void testSetContactInfo() {
        User user = this.mockUser();
        assertNull(user.getContactInfo());
        user.setContactInfo("testContactInfo");
        assertEquals("testContactInfo", user.getContactInfo());
    }

    @Test
    public void testGetPassword() {
        User user = this.mockUser();
        assertNull(user.getPassword());
    }

    @Test
    public void testSetPassword() {
        User user = this.mockUser();
        assertNull(user.getPassword());
        user.setPassword("testPassword");
        assertEquals("testPassword", user.getPassword());
    }
}
