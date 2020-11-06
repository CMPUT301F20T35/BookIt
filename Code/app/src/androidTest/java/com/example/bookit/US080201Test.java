package com.example.bookit;

import android.widget.EditText;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;

public class US080201Test {
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

        solo.clickOnView(solo.getView(R.id.navigation_my_book));
        solo.clickOnButton("Available");
        solo.waitForFragmentByTag("MyBookAvailableFragment", 5000);
        // delete book image
        solo.clickInRecyclerView(0);
        solo.waitForFragmentByTag("BookDetail", 5000);
        solo.clickOnView(solo.getView(R.id.deleteButton));

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
