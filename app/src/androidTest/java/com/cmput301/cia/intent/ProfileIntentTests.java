/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 12 2017
 *
 * This class tests the UI for viewing a user's profile
 * NOTE: These tests require an internet connection
 */

// TODO: viewing other people's profiles, following/saving

public class ProfileIntentTests extends ActivityInstrumentationTestCase2<HomePageActivity> {

    private Solo solo;

    public ProfileIntentTests() {
        super(com.cmput301.cia.activities.HomePageActivity.class);
    }

    public void setUp() throws Exception{

        Profile profile = new TestProfile("xyz");
        Intent intent = new Intent();
        intent.putExtra(HomePageActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test to make sure the profile being viewed is the signed in user's profile
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testViewedProfile() throws NoSuchFieldException, IllegalAccessException {

        solo.clickOnActionBarItem(R.id.menu_button_My_Profile);
        solo.clickOnMenuItem("My Profile");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("displayed");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(solo.getCurrentActivity());

        Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("viewer");
        field2.setAccessible(true);
        Profile user = (Profile)field.get(solo.getCurrentActivity());

        assertTrue(viewed.equals(user));
    }

    /**
     * Test selecting the save button and making sure that the comment and image save as a result
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testSave() throws NoSuchFieldException, IllegalAccessException {

        String newComment = "this is a test comment @das";

        Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field2.setAccessible(true);
        Profile user = (Profile)field2.get(solo.getCurrentActivity());
        user.setComment("");
        assertFalse(user.getComment().equals(newComment));

        solo.clickOnActionBarItem(R.id.menu_button_My_Profile);
        solo.clickOnMenuItem("My Profile");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("displayed");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(solo.getCurrentActivity());

        viewed.setComment("");
        assertFalse(viewed.getComment().equals(newComment));

        solo.clearEditText((EditText)solo.getView(R.id.profileCommentDynamicText));
        solo.sleep(500);
        solo.enterText((EditText)solo.getView(R.id.profileCommentDynamicText), newComment);
        solo.clickOnButton("Save");
        solo.sleep(4000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        assertTrue(user.getComment().equals(newComment));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}