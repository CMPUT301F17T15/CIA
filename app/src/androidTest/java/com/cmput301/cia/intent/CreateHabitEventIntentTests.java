/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitEventActivity;
import com.robotium.solo.Solo;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 5 2017
 *
 * This class tests the UI for creating habit events
 */

public class CreateHabitEventIntentTests extends ActivityInstrumentationTestCase2<CreateHabitEventActivity> {

    private Solo solo;

    public CreateHabitEventIntentTests() {
        super(com.cmput301.cia.activities.CreateHabitEventActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testCommentLength(){
        solo.enterText((EditText)solo.getView(R.id.cheCommentEditText), "@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD");
        // max length = 20
        assertFalse(solo.waitForText("@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD", 1, 2000));
        assertTrue(((EditText) solo.getView(R.id.cheCommentEditText)).getText().toString().length() == 20);
    }

    public void testFinish(){
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Finish");

        // TODO: previous activity
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        // TODO: get habit events count from class
        int habitEvents = 0;

        // TODO: button text for getting back to create habit event activity
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Save");

        // TODO: assert new habit events count is old+1
        assertTrue(false);
    }

    public void testCancel(){
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Cancel");

        // TODO: previous activity
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        // TODO: get habit events count from class
        int habitEvents = 0;

        // TODO: button text for getting back to create habit event activity
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Cancel");

        // TODO: assert new habit events count is equal to old
        assertTrue(false);

    }

    public void testSelectImage(){

        // TODO: index?
        solo.clickOnImage(0);

        // TODO: test picking image

        //Bitmap image = ((CreateHabitEventActivity)solo.getCurrentActivity());

        // TODO: assert image size <= CreateHabitEventActivity.MAX_IMAGE_SIZE

    }

    /*public void testStart() throws Exception {
        Activity activity = getActivity();
    }*/

    /*
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