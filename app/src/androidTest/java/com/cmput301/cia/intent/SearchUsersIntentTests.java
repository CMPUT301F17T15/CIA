/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.HomeTabbedActivity;
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
 * This class tests the UI for searching for user profiles
 * NOTE: These tests require an internet connection
 */

public class SearchUsersIntentTests extends ActivityInstrumentationTestCase2<HomeTabbedActivity> {

    private Solo solo;

    public SearchUsersIntentTests() {
        super(com.cmput301.cia.activities.HomeTabbedActivity.class);
    }

    public void setUp() throws Exception{

        Profile profile = new TestProfile("xyz");
        profile.setFirstTimeUse(false);
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
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_search));
        solo.sleep(1000);

        solo.enterText(0, "vfutest");
        solo.sleep(600);
        solo.clickOnButton("Search");
        solo.sleep(2500);
        ListAdapter adapter = ((ListView)solo.getView(R.id.searchUsersList)).getAdapter();
        for (int i = 0; i < adapter.getCount(); ++i){
            assertTrue(((Profile)adapter.getItem(i)).getName().contains("vfutest"));
        }
    }

    public void testSelectUser(){
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_search));
        solo.sleep(1000);

        ListAdapter adapter = ((ListView)solo.getView(R.id.searchUsersList)).getAdapter();
        if (adapter.getCount() > 0) {
            solo.clickInList(1, 0);
            solo.sleep(2000);
            assertTrue("wrong fragment", getActivity().getFragmentForCurrentTab() instanceof SearchUsersActivity);
        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}