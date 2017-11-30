/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.unit;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Pair;

import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 24 2017
 *
 * Tests the geolocation aspects of habit events
 * NOTE: assumes device is connected to the internet. Will not succeed if it isn't.
 */

@RunWith(AndroidJUnit4.class)
public class GeolocationUnitTests {

    @Test
    public void testNearbyEvents() throws InterruptedException {

        Habit habit = new Habit("XYZ", "", new Date(), new ArrayList<Integer>(), "Type");
        Habit habit2 = new Habit("DBZ", "", new Date(), new ArrayList<Integer>(), "Category");
        Habit habit3 = new Habit("DBZ", "", new Date(), new ArrayList<Integer>(), "Category");
        habit.setId("XYZ");
        habit2.setId("DBZ");
        habit3.setId("YYY");

        Map<String, String> values = new HashMap<>();
        values.put("name", "gut1");
        Profile followee = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", followee != null);
        values.put("name", "gut2");
        Profile user = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", user != null);

        // reset profiles from previous runs
        followee.removeFollowRequest(user);
        user.removeFollowRequest(followee);
        followee.unfollow(user);
        user.unfollow(followee);
        followee.setHabits(new ArrayList<Habit>());
        user.setHabits(new ArrayList<Habit>());

        user.addHabit(habit);
        followee.addHabit(habit2);
        followee.addHabit(habit3);

        followee.addFollowRequest(user);
        Thread.sleep(1000);
        followee.acceptFollowRequest(user);
        Thread.sleep(1000);

        // location of both profiles
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(10.0);
        location.setLongitude(10.0);

        assertTrue(followee.save());
        Thread.sleep(1000);

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
        assertTrue(followee.save());
        Thread.sleep(2000);

        assertTrue(user.getNearbyEvents(location).size() == 4);
        assertTrue(followee.getNearbyEvents(location).size() == 1);

        // add a new event for the followee that is too far away. it is the most recent event so count decreases for user
        followee.tryHabitEvent(new AddHabitEvent("DBZ", new HabitEvent("X", "", new Date(), 50.0, 50.0)));
        assertTrue(followee.save());
        Thread.sleep(2000);

        assertTrue(user.getNearbyEvents(location).size() == 3);
        assertTrue(followee.getNearbyEvents(location).size() == 1);

        // add a new event for the followee -> the user's nearby events list size is still 4 since only the most recent event of each type
        // shows up from followed users
        followee.tryHabitEvent(new AddHabitEvent("DBZ", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(followee.save());
        Thread.sleep(2000);

        assertTrue(user.getNearbyEvents(location).size() == 4);
        assertTrue(followee.getNearbyEvents(location).size() == 2);

        // add a new event for the followee in a new category --> shows up for both user and followee
        followee.tryHabitEvent(new AddHabitEvent("YYY", new HabitEvent("X", "", new Date(), 10.0, 10.0)));
        assertTrue(followee.save());
        Thread.sleep(2000);

        assertTrue(user.getNearbyEvents(location).size() == 5);
        assertTrue(followee.getNearbyEvents(location).size() == 3);

    }


}
