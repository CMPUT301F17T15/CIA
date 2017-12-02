/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.users.SearchUsersActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 30 2017
 *
 * This class tests the UI for searching for user profiles
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
        ListAdapter adapter = ((ListView)solo.getView(R.id.searchUsersList)).getAdapter();
        if (adapter.getCount() > 0) {
            solo.clickInList(1, 0);
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);
        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}