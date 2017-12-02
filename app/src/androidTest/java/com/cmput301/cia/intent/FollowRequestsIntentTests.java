/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.util.Pair;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.users.FollowRequestsActivity;
import com.cmput301.cia.controller.FollowersRequestAdapter;
import com.cmput301.cia.models.Follow;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Dec 01 2017
 *
 * This class tests the UI for viewing and responding to follow requests from other users
 * NOTE: These tests require an internet connection
 */

public class FollowRequestsIntentTests extends ActivityInstrumentationTestCase2<FollowRequestsActivity> {

    private Solo solo;

    public FollowRequestsIntentTests() {
        super(FollowRequestsActivity.class);
    }

    public void setUp() throws Exception{

        // initialize all profiles that will be following another user
        List<Profile> profiles = new ArrayList<>();

        // whether the database needs to be reset and have these profiles be recreated
        boolean needToReset = false;

        for (int i = 0; i < 15; ++i){
            Profile profile = new Profile("frtest" + i);

            Map<String, String> map = new HashMap<>();
            map.put("name", profile.getName());
            Pair<Profile, Boolean> result = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, map);

            assertTrue("database error", result.second);
            if (result.first != null) {
                profile = result.first;
            } else {
                assertTrue("database error", profile.save());
                needToReset = true;
            }

            profiles.add(profile);
        }

        if (profiles.get(0).getFollowing().size() < 2)
            needToReset = true;

        if (needToReset) {

            Thread.sleep(1000);
            // remove existing follows from previous runs
            for (int i = 0; i < profiles.size(); ++i) {
                Map<String, String> map = new HashMap<>();
                map.put("followerId", profiles.get(i).getId());
                ElasticSearchUtilities.delete(Follow.TYPE_ID, Follow.class, map);

                map.clear();
                map.put("followeeId", profiles.get(i).getId());
                ElasticSearchUtilities.delete(Follow.TYPE_ID, Follow.class, map);
            }

            // make all profiles follow the first one
            for (int i = 1; i < profiles.size(); ++i) {
                profiles.get(0).addFollowRequest(profiles.get(i));
            }

            Thread.sleep(1000);
        }

        Intent intent = new Intent();
        intent.putExtra(FollowRequestsActivity.ID_PROFILE, profiles.get(0));
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test to make sure accepting and declining follow requests works
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testResponse() throws NoSuchFieldException, IllegalAccessException {
        FollowersRequestAdapter adapter = (FollowersRequestAdapter) ((RecyclerView)solo.getView(R.id.rvFollowerRequests)).getAdapter();

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile)field.get(solo.getCurrentActivity());

        // accept the first user in the view's request
        int size = adapter.getItemCount();
        Profile profile = adapter.getProfile(0);
        solo.clickInRecyclerView(0, 0, R.id.approveButton);
        solo.sleep(1500);
        // remove item
        assertTrue("database error", size == adapter.getItemCount() + 1);
        // make sure the profile is no longer in the view
        for (int i = 0; i < adapter.getItemCount(); ++i){
            assertFalse(adapter.getProfile(i).equals(profile));
        }
        assertTrue("database error", profile.isFollowing(user));
        assertFalse("database error", user.hasFollowRequest(profile));
        profile.unfollow(user);
        solo.sleep(2000);
        assertFalse("database error", profile.isFollowing(user));

        // reject the first user in the view's request
        profile = adapter.getProfile(0);
        solo.clickInRecyclerView(0, 0, R.id.declineButton);
        solo.sleep(1500);
        // remove item
        assertTrue("database error", size == adapter.getItemCount() + 2);
        // make sure the profile is no longer in the view
        for (int i = 0; i < adapter.getItemCount(); ++i){
            assertFalse(adapter.getProfile(i).equals(profile));
        }
        assertFalse(profile.isFollowing(user));
        assertFalse("database error", user.hasFollowRequest(profile));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}