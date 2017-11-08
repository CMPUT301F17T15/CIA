/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.DeleteHabitEvent;
import com.cmput301.cia.models.EditHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adil on Oct 18 2017.
 * Version 1
 *
 * Tests the offline events that will be synchronized with the database when the user
 * regains connectivity.
 *
 */

public class OfflineUnitTests extends ActivityInstrumentationTestCase2 {

    public OfflineUnitTests() {
        super(MainActivity.class);
    }

    // TODO: find way to force device offline and then integrate ElasticSearch
    public void testOfflineEditEvent(){
        Profile profile = new Profile("Name");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        HabitEvent newEvent = new HabitEvent("DBZ", new Date());
        OfflineEvent event = new EditHabitEvent(old, newEvent);

        profile.tryHabitEvent(event);
        assertFalse(old.getComment().equals(newEvent.getComment()));
        profile.synchronize();
        assertTrue(old.getComment().equals(newEvent.getComment()));
    }

    public void testOfflineAddEvent(){
        Profile profile = new Profile("Name");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        OfflineEvent event = new AddHabitEvent("Name", "Habit", old);
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));

        profile.tryHabitEvent(event);
        assertFalse(profile.getHabits().get(0).getTimesCompleted() == 1);
        profile.synchronize();
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
    }

    public void testOfflineDeleteEvent(){
        Profile profile = new Profile("Name");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        OfflineEvent event = new DeleteHabitEvent("Name", "Habit", old);
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.getHabits().get(0).addHabitEvent(old);

        profile.tryHabitEvent(event);
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
        profile.synchronize();
        assertFalse(profile.getHabits().get(0).getTimesCompleted() == 1);
    }

}
