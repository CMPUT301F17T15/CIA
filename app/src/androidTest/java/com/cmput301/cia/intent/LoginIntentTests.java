/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 3 2017
 *
 * This class tests the UI for logging into the system and creating
 * a new profile
 */

public class LoginIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public LoginIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testCreateProfile(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Login");            // can not login with empty name
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Register");         // can not register with empty name
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "TestSignIn");
        solo.clickOnButton("Login");            // can not login since profile should not exist
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Register");         // register new profile
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // TODO: test to make sure previously created profiles can not be recreated
    }

    /*public void testStart() throws Exception {
        Activity activity = getActivity();
    }*/

    /*public void testTweet(){
        solo.assertCurrentActivity("wrong activity", LonelyTwitterActivity.class);
        solo.clickOnButton("Clear");
        solo.enterText((EditText)solo.getView(R.id.body), "Test Tweet!");
        // text on button
        solo.clickOnButton("Save");
        solo.clearEditText((EditText)solo.getView(R.id.body));
        // wait until that text is on the screen somewhere
        assertTrue(solo.waitForText("Test Tweet!", 1, 1000));
        solo.clickOnButton("Clear");
        assertFalse(solo.waitForText("Test Tweet!", 1, 1000));
    }
    public void testClickTweetList(){
	    LonelyTwitterActivity activity = (LonelyTwitterActivity) solo.getCurrentActivity();
        final ListView oldTweetsList = activity.getOldTweetsList();
        Tweet tweet = (Tweet)oldTweetsList.getItemAtPosition(0);
        //assertEquals("Test Tweet!", tweet.getMessage());
        solo.clickInList(0);
        solo.assertCurrentActivity("wrong activity", EditTweetActivity.class);
        assertFalse(solo.waitForText("New Activity", 1, 1000));
        solo.goBack();
        solo.assertCurrentActivity("wrong activity", LonelyTwitterActivity.class);
    }*/

    /*public void testEditTweet(){

        LonelyTwitterActivity activity = (LonelyTwitterActivity) solo.getCurrentActivity();

        final ListView oldTweetsList = activity.getOldTweetsList();
        Tweet tweet = (Tweet)oldTweetsList.getItemAtPosition(0);

        solo.clickInList(0);
        solo.assertCurrentActivity("wrong activity", EditTweetActivity.class);
        solo.clearEditText((EditText)solo.getView(R.id.msgEditText));
        solo.enterText((EditText)solo.getView(R.id.msgEditText), "Test Tweet!");
        assertTrue(solo.waitForText("Test Tweet!", 1, 1000));
        solo.clickOnButton("Finish");
        solo.assertCurrentActivity("wrong activity", LonelyTwitterActivity.class);

        assertEquals("Test Tweet!", tweet.getMessage());
    }*/

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}