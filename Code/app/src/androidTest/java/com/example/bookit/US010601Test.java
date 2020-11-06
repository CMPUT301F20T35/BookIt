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

public class US010601Test {
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
        // edit book
        solo.clickInRecyclerView(0);
        solo.clickOnView(solo.getView(R.id.editbookinfo));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "1");
        solo.waitForText("testTitle1", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.edit_author), "1");
        solo.waitForText("testAuthor1", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "test");
        solo.waitForText("testUS010601test", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.edit_des), "1");
        solo.waitForText("testDescription1", 1, 2000);
        solo.clickOnButton("update");
        solo.clickOnButton("finish");
        //compare edit is success
        solo.clickInRecyclerView(0);
        assertEquals("testTitle1", solo.getText("testTitle1").getText().toString());
        assertEquals("testAuthor1", solo.getText("testAuthor1").getText().toString());
        assertEquals("testUS010101test", solo.getText("testUS010101test").getText().toString());
        assertEquals("testDescription1", solo.getText("testDescription1").getText().toString());

    }
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
