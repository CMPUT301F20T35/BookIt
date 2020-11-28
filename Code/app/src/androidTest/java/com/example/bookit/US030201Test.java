package com.example.bookit;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class US030201Test {
    private Solo solo;

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<Login>(Login.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void test(){
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        // login with test account
        solo.enterText((EditText) solo.getView(R.id.emailText), "test@gmail.com");
        solo.waitForText("test@gmail.com", 1, 1000);
        solo.enterText((EditText) solo.getView(R.id.password), "test123456");
        solo.waitForText("test123456", 1, 1000);
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // go to BorrowSearchFragment
        solo.clickOnView(solo.getView(R.id.navigation_borrow_book));
        solo.clickOnButton("Available");
        solo.clickOnView(solo.getView(R.id.button_search));
        solo.waitForFragmentByTag("BorrowSearchFragment", 2000);

        // search a book by input keyword
        solo.enterText((EditText) solo.getView(R.id.text_search), "US030101");
        solo.waitForText("US030101", 1, 2000);
        solo.clickOnView(solo.getView(R.id.button_search));
        solo.waitForFragmentByTag("BorrowSearchFragment", 5000);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
