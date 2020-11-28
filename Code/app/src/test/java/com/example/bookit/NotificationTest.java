package com.example.bookit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NotificationTest {

    // mock Notification object
    private Notification mockNotification() {
        Notification notification = new Notification(null, null, null, null, null, null);
        return notification;
    }

    // Tests for functionality of Notification class
    @Test
    public void testGetTitle() {
        Notification notification = mockNotification();
        assertNull(notification.getTitle());
    }

    @Test
    public void testSetTitle() {
        Notification notification = mockNotification();
        assertNull(notification.getTitle());
        notification.setTitle("testTitle");
        assertEquals("testTitle", notification.getTitle());
    }

    @Test
    public void testGetISBN() {
        Notification notification = mockNotification();
        assertNull(notification.getISBN());
    }

    @Test
    public void testSetISBN() {
        Notification notification = mockNotification();
        assertNull(notification.getISBN());
        notification.setISBN("testISBN");
        assertEquals("testISBN", notification.getISBN());
    }

    @Test
    public void testGetOwnerName() {
        Notification notification = mockNotification();
        assertNull(notification.getOwnerName());
    }

    @Test
    public void testSetOwnerName() {
        Notification notification = mockNotification();
        assertNull(notification.getOwnerName());
        notification.setOwnerName("testOwnerName");
        assertEquals("testOwnerName", notification.getOwnerName());
    }

    @Test
    public void testGetRequestors() {
        Notification notification = mockNotification();
        assertNull(notification.getRequesters());
    }

    @Test
    public void testSetRequestors() {
        Notification notification = mockNotification();
        assertNull(notification.getRequesters());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test1");
        arrayList.add("test2");
        notification.setRequesters(arrayList);
        assertEquals(arrayList, notification.getRequesters());
    }

    @Test
    public void testGetNotificationType() {
        Notification notification = mockNotification();
        assertNull(notification.getNotificationType());
    }

    @Test
    public void testSetNotificationType() {
        Notification notification = mockNotification();
        assertNull(notification.getNotificationType());
        notification.setNotificationType("testNotificationType");
        assertEquals("testNotificationType", notification.getNotificationType());
    }

    @Test
    public void testGetAcceptedUser() {
        Notification notification = mockNotification();
        assertNull(notification.getAcceptedUser());
    }

    @Test
    public void testSetAcceptedUser() {
        Notification notification = mockNotification();
        assertNull(notification.getAcceptedUser());
        notification.setAcceptedUser("testAcceptedUser");
        assertEquals("testAcceptedUser", notification.getAcceptedUser());
    }

}
