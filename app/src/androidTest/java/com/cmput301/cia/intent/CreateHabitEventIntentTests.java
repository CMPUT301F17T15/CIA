/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitEventActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 5 2017
 *
 * This class tests the UI for creating habit events
 * NOTE: some of these tests require an internet connection (for saving in the ElasticSearch database)
 */

public class CreateHabitEventIntentTests extends ActivityInstrumentationTestCase2<HomePageActivity> {

    private Solo solo;

    public CreateHabitEventIntentTests() {
        super(com.cmput301.cia.activities.HomePageActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testCommentLength(){
        solo.clickInList(1, 1);
        solo.enterText((EditText)solo.getView(R.id.cheCommentEditText), "@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD");
        // max length = 20
        assertFalse(solo.waitForText("@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD", 1, 2000));
        assertTrue(((EditText) solo.getView(R.id.cheCommentEditText)).getText().toString().length() == 20);
    }

    public void testFinish() throws NoSuchFieldException, IllegalAccessException {
        // Select the 1st option in the second list (the "today's tasks" list)
        solo.clickInList(1, 1);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());
        int habitEvents = user.getHabitHistory().size();

        solo.clickInList(1, 1);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Save");

        // assert that a new event was added
        assertTrue(user.getHabitHistory().size() == habitEvents + 1);
    }

    public void testCancel() throws NoSuchFieldException, IllegalAccessException {
        solo.clickInList(1, 1);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Cancel");

        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());
        int habitEvents = user.getHabitHistory().size();

        solo.clickInList(1, 1);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Cancel");

        // assert new habit events count is equal to old
        assertTrue(user.getHabitHistory().size() == habitEvents);
    }

    public void testSelectImage(){
        solo.clickInList(1, 1);
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