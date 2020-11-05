package com.example.bookit;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class US020201Test {
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

        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.waitForFragmentByTag("ProfileFragment", 5000);

        // test editing your profile
        solo.clickOnView(solo.getView(R.id.editButton));
        solo.waitForDialogToOpen(1000);
        solo.clearEditText((EditText) solo.getView(R.id.contactinfoedit));
        solo.enterText((EditText) solo.getView(R.id.contactinfoedit),"(000) 000-0000");
        solo.clickOnButton("update");
        solo.waitForDialogToClose(1000);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
