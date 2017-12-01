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
import com.cmput301.cia.activities.users.SearchUsersActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 30 2017
 *
 * This class tests the UI for searching for user's profiles
 * NOTE: These tests require an internet connection
 */

public class SearchUsersIntentTests extends ActivityInstrumentationTestCase2<SearchUsersActivity> {

    private Solo solo;

    public SearchUsersIntentTests() {
        super(com.cmput301.cia.activities.users.SearchUsersActivity.class);
    }

    public void setUp() throws Exception{

        Profile profile = new TestProfile("xyz");
        Intent intent = new Intent();
        intent.putExtra(SearchUsersActivity.ID_USER, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test to make sure the profile being viewed is the signed in user's profile
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testFilter() throws NoSuchFieldException, IllegalAccessException {

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

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}