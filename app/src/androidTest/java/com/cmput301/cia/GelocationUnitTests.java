/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adil on Nov 08 2017.
 * Version 1
 *
 * Tests the geolocation aspects of habit events
 */

public class GelocationUnitTests extends ActivityInstrumentationTestCase2 {

    public GelocationUnitTests() {
        super(HabitEvent.class);
    }

    public void testNearbyEvents(){

        Habit habit = new Habit("XYZ", "", new Date(), new ArrayList<Integer>(), "Type");
        Habit habit2 = new Habit("DBZ", "", new Date(), new ArrayList<Integer>(), "Category");
        Habit habit3 = new Habit("DBZ", "", new Date(), new ArrayList<Integer>(), "Category");
        habit.setId("XYZ");
        habit2.setId("DBZ");
        habit3.setId("YYY");

        Profile user = new Profile("User");
        Profile followee = new Profile("Followee");

        user.addHabit(habit);
        followee.addHabit(habit2);

        followee.addFollowRequest(user);
        followee.acceptFollowRequest(user);

        // location of both profiles
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(10.0);
        location.setLongitude(10.0);

        // no events for either user
        assertTrue(user.getNearbyEvents(location).size() == 0);
        assertTrue(followee.getNearbyEvents(location).size() == 0);

        OfflineEvent event = new AddHabitEvent("XYZ", new HabitEvent("X", "", new Date(), 50.0, 50.0));
        user.tryHabitEvent(event);

        // user is too far from the added event, and the followee is not following the user -> both lists should be empty
        assertTrue(user.getNearbyEvents(location).size() == 0);
        assertTrue(followee.getNearbyEvents(location).size() == 0);

        // 3 new events at the exact same location
        user.tryHabitEvent(new AddHabitEvent("XYZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        user.tryHabitEvent(new AddHabitEvent("XYZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        user.tryHabitEvent(new AddHabitEvent("XYZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(user.getNearbyEvents(location).size() == 3);
        assertTrue(followee.getNearbyEvents(location).size() == 0);

        // add a new event for the followee -> should show up in user's nearby events list since that includes followees
        followee.tryHabitEvent(new AddHabitEvent("DBZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(user.getNearbyEvents(location).size() == 4);
        assertTrue(followee.getNearbyEvents(location).size() == 1);

        // add a new event for the followee that is too far away
        followee.tryHabitEvent(new AddHabitEvent("DBZ", new HabitEvent("X", "", new Date(), 50.0, 50.0)));
        assertTrue(user.getNearbyEvents(location).size() == 4);
        assertTrue(followee.getNearbyEvents(location).size() == 1);

        // add a new event for the followee -> the user's nearby events list size is still 4 since only the most recent event of each type
        // shows up from followed users
        followee.tryHabitEvent(new AddHabitEvent("DBZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(user.getNearbyEvents(location).size() == 4);
        assertTrue(followee.getNearbyEvents(location).size() == 2);

        // add a new event for the followee in a new category --> shows up for both user and followee
        followee.tryHabitEvent(new AddHabitEvent("YYY", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(user.getNearbyEvents(location).size() == 5);
        assertTrue(followee.getNearbyEvents(location).size() == 3);

    }


}
