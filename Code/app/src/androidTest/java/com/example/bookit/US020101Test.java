package com.example.bookit;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class US020101Test {
    private Solo solo;

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<Login>(Login.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void Test() {
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        // login with test account
        solo.enterText((EditText) solo.getView(R.id.emailText), "test@gmail.com");
        solo.waitForText("test@gmail.com", 1, 1000);
        solo.enterText((EditText) solo.getView(R.id.password), "test123456");
        solo.waitForText("test123456", 1, 1000);
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check the user name and contact Info
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.waitForFragmentByTag("ProfileFragment", 5000);

        // you can test if your Info are correct if you want to, however, be cautious if any other test change your Info
//        assertEquals("testUser", solo.getText("test").getText().toString());
//        assertEquals("(000) 000-0000", solo.getText("(000) 000-0000").getText().toString());
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
