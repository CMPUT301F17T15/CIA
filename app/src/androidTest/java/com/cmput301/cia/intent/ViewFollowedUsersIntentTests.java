/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.activities.users.ViewFollowedUsersActivity;
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Follow;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Version 3
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for viewing details about the profiles that a user is following
 * NOTE: These tests require an internet connection
 */

public class ViewFollowedUsersIntentTests extends ActivityInstrumentationTestCase2<ViewFollowedUsersActivity> {

    private Solo solo;

    // the profile that is following all the other profiles
    private static Profile profile = null;

    public ViewFollowedUsersIntentTests() {
        super(ViewFollowedUsersActivity.class);
    }

    /**
     * initialize all necessary profiles if they have not been yet
     * @return the profile that is following all the other profiles
     * @throws InterruptedException
     */
    private Profile init() throws InterruptedException {

        if (profile != null)
            return profile;

        // initialize all profiles that will be following another user
        List<Profile> profiles = new ArrayList<>();

        // whether the database needs to be reset and have these profiles be recreated
        boolean needToReset = false;

        for (int i = 0; i < 15; ++i){
            Profile profile = new Profile("vfutest" + i);

            Map<String, String> map = new HashMap<>();
            map.put("name", profile.getName());
            Pair<Profile, Boolean> result = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, map);

            assertTrue("database error", result.second);
            if (result.first != null) {
                profile = result.first;
                if (i == 2){
                    profiles.get(1).addFollowRequest(profiles.get(0));
                    Thread.sleep(200);
                    profiles.get(1).acceptFollowRequest(profiles.get(0));
                }
            } else {
                assertTrue("database error", profile.save());
                needToReset = true;
            }

            profiles.add(profile);
        }

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

            // for all profiles, generate 3 habits for the test
            for (int i = 0; i < profiles.size(); ++i) {
                for (int x = 0; x < 3; ++x) {
                    String habitName = new StringBuilder(profiles.get(i).getId()).reverse().toString();
                    profiles.get(i).addHabit(new Habit(habitName, "", new Date(), new ArrayList<Integer>(), "xyz"));

                    if (x == 1) {
                        profiles.get(i).getHabits().get(x).addHabitEvent(new HabitEvent("xyz"));
                    }
                }
            }

            // make all profiles follow the first one
            for (int i = 1; i < profiles.size(); ++i) {
                profiles.get(i).addFollowRequest(profiles.get(0));
                Thread.sleep(200);
                profiles.get(i).acceptFollowRequest(profiles.get(0));
            }

            // save all profiles due to how follow information is stored
            for (int i = 0; i < profiles.size(); ++i)
                assertTrue("database error", profiles.get(i).save());
            Thread.sleep(1000);
        }

        profile = profiles.get(0);
        return profile;
    }

    public void setUp() throws Exception {

        Intent intent = new Intent();
        intent.putExtra(ViewFollowedUsersActivity.ID_VIEWED, init());
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * test the visibility of the views in the activity as certain buttons as pressed
     */
    public void testVisibility() {
        assertTrue(solo.getView(R.id.vfuHabitsList).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuEventsList).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuUsersIcon).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryIcon).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryEventsSwitch).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuMapView).getVisibility() == View.INVISIBLE);

        // switch to history view
        solo.clickOnView(solo.getView(R.id.vfuHistoryIcon));
        solo.sleep(600);

        // habits list should still be invisible
        assertTrue(solo.getView(R.id.vfuHabitsList).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuEventsList).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuUsersIcon).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryIcon).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryEventsSwitch).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuMapView).getVisibility() == View.VISIBLE);

        // habits list should now be visible
        solo.clickOnView(solo.getView(R.id.vfuHistoryEventsSwitch));
        solo.sleep(600);
        assertTrue(solo.getView(R.id.vfuHabitsList).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuEventsList).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuUsersIcon).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryIcon).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryEventsSwitch).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuMapView).getVisibility() == View.VISIBLE);

        // switch back to profile view
        solo.clickOnView(solo.getView(R.id.vfuUsersIcon));
        solo.sleep(600);

        assertTrue(solo.getView(R.id.vfuHabitsList).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuEventsList).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuUsersIcon).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryIcon).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.vfuHistoryEventsSwitch).getVisibility() == View.INVISIBLE);
        assertTrue(solo.getView(R.id.vfuMapView).getVisibility() == View.INVISIBLE);
    }

    /**
     * test to make sure all followed profiles show up in the list
     */
    public void testFollowingList(){

        ListAdapter adapter = ((ListView)solo.getView(R.id.vfuProfilesList)).getAdapter();

        for (Profile followed : profile.getFollowing()){

            boolean found = false;
            for (int i = 0; i < adapter.getCount(); ++i){
                if (adapter.getItem(i).equals(followed)){
                    found = true;
                    break;
                }
            }

            assertTrue("profile not found in followed users listview", found);
        }
    }

    /**
     * test to make sure all followed profile's habits show up in the list
     */
    public void testFollowingHabits(){

        // switch to history view
        solo.clickOnView(solo.getView(R.id.vfuHistoryIcon));
        solo.sleep(600);

        // habits list should now be visible
        solo.clickOnView(solo.getView(R.id.vfuHistoryEventsSwitch));
        solo.sleep(600);

        ListAdapter adapter = ((ListView)solo.getView(R.id.vfuHabitsList)).getAdapter();
        for (Profile followed : profile.getFollowing()){
            for (Habit habit : followed.getHabits()) {

                boolean found = false;
                for (int i = 0; i < adapter.getCount(); ++i) {
                    if (adapter.getItem(i).equals(habit)) {
                        found = true;
                        break;
                    }
                }

                assertTrue("habit not found in followed user habits listview", found);
            }
        }
    }

    /**
     * test to make sure all followed profile's habit events show up in the list, and are in sorted order
     */
    public void testFollowingEvents(){
        // switch to history view
        solo.clickOnView(solo.getView(R.id.vfuHistoryIcon));
        solo.sleep(600);

        ListAdapter adapter = ((ListView)solo.getView(R.id.vfuEventsList)).getAdapter();
        for (CompletedEventDisplay event : profile.getFollowedHabitHistory()){
            boolean found = false;
            for (int i = 0; i < adapter.getCount(); ++i) {
                if (adapter.getItem(i).equals(event)) {
                    found = true;
                    break;
                }
            }
            assertTrue("habit not found in followed user habits listview", found);
        }
    }

    /**
     * test to make sure all views update when a user is unfollowed from this activity
     */
    public void testUnfollowUpdate(){

        int count = ((ListView)solo.getView(R.id.vfuProfilesList)).getAdapter().getCount();

        solo.clickOnView(((ListView)solo.getView(R.id.vfuProfilesList)).getAdapter().getView(0, null, null));
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        solo.clickOnButton("Unfollow");
        solo.sleep(1000);

        solo.goBack();
        solo.sleep(3000);

        assertTrue("profile was not removed from list", ((ListView)solo.getView(R.id.vfuProfilesList)).getAdapter().getCount() == count - 1);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}