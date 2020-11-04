package com.example.bookit;

import android.widget.EditText;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class US010101Test {
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
        solo.clickOnView(solo.getView(R.id.button_add));

        // input book information
        solo.enterText((EditText) solo.getView(R.id.newBookTitle), "testTitle");
        solo.waitForText("testTitle", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.newBookAuthor), "testAuthor");
        solo.waitForText("testAuthor", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.newBookISBN), "testUS010101");
        solo.waitForText("testUS010101", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.newBookDescription), "testDescription");
        solo.waitForText("testDescription", 1, 2000);
        solo.clickOnView(solo.getView(R.id.addNewBookBtn));

        solo.waitForFragmentByTag("MyBookAvailableFragment", 5000);
        solo.clickInRecyclerView(0);
        assertEquals("testTitle", solo.getText("testTitle").getText().toString());
        assertEquals("testAuthor", solo.getText("testAuthor").getText().toString());
        assertEquals("testUS010101", solo.getText("testUS010101").getText().toString());
        assertEquals("testDescription", solo.getText("testDescription").getText().toString());
    }
}
