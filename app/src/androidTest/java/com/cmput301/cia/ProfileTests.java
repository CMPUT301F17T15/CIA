/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WTH on 2017-10-22.
 */

public class ProfileTests extends ActivityInstrumentationTestCase2 {
    
    public ProfileTests() {super(MainActivity.class); }

    /**
     * following
     */
    public void testAddRequest(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        assertTrue(profile.hasFollowRequest(request));
        assertFalse(request.hasFollowRequest(profile));
    }

    public void testRemoveRequest(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        profile.removeFollowRequest(request);
        assertFalse(profile.hasFollowRequest(request));
    }

    public void testFollowing(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        profile.acceptFollowRequest(request);
        assertFalse(profile.hasFollowRequest(request));
        assertTrue(request.isFollowing(profile));
        assertFalse(request.hasFollowRequest(profile));
    }

    public void testFollowedHistory(){
        Profile profile = new Profile("name");
        Profile request = new Profile("Mike");
        profile.addFollowRequest(request);
        profile.acceptFollowRequest(request);
        List<HabitEvent> eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 0);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>()));
        assertTrue(request.getFollowedHabitHistory().size() == 1);
        profile.getHabits().get(0).addHabitEvent(new HabitEvent("XYZ"));
        eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 1);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
    }

}
