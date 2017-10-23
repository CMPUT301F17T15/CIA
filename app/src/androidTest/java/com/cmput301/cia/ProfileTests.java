/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.models.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WTH on 2017-10-22.
 */

public class ProfileTests extends ActivityInstrumentationTestCase2 {
    public ProfileTests() {super(MainActivity.class); }

    public void testUser(){
        String username = "name";
        Profile user = new Profile(username);
        assertEquals(user.getName(), username);
    }

    public void testFollowUser(){
        Profile requestTest = new Profile("name");
        Profile test = new Profile("name");
        requestTest.getFollowing();
    }
    /**
     * share
     */
    public void testShareUser(){
        Profile requestTest = new Profile("name");
        Profile test = new Profile("name");
        requestTest.getFollowing();

    }

    /**
     * following
     */
    public void testAddRequest(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(profile);
        assertTrue(profile.hasFollowRequest(profile));
    }

    public void testRemoveRequest(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(profile);
        profile.removeFollowRequest(profile);
        assertFalse(profile.hasFollowRequest(profile));
    }

    public void testFollowing(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        request.follow(profile);
        assertFalse(profile.hasFollowRequest(profile));
    }

    public void testFollowedHistory(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        request.follow(profile);
        List<HabitEvent> eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 0);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>()));
        profile.getHabits().get(0).addHabitEvent(new HabitEvent("XYZ"));
        eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 0);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
    }







}
