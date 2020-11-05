package com.example.bookit;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpValidatorTest {

    // Tests for functionality of signUpValidator class
    @Test
    public void testNumberCheck(){
        signUpValidator test1 = new signUpValidator(null, null, null, "5879361971");
        assertTrue(test1.numberCheck());
        signUpValidator test2 = new signUpValidator(null, null, null, "?dw123&89*");
        assertFalse(test2.numberCheck());
    }

    @Test
    public void testPasswordCheck(){
        signUpValidator test1 = new signUpValidator("123456", null, null, null);
        assertTrue(test1.passwordCheck());
        signUpValidator test2 = new signUpValidator("12345", null, null, null);
        assertFalse(test2.passwordCheck());
    }
}
