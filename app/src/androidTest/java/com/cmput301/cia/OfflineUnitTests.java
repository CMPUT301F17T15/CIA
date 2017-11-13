/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.support.test.runner.AndroidJUnit4;
import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.DeleteHabitEvent;
import com.cmput301.cia.models.EditHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adil on Oct 18 2017.
 * Version 1
 *
 * Tests the offline events that will be synchronized with the database when the user
 * regains connectivity.
 *
 */

@RunWith(AndroidJUnit4.class)
public class OfflineUnitTests {

    // TODO: find way to force device offline and then integrate ElasticSearch
    // ex: WifiManager
    @Test
    public void testOfflineEditEvent(){
        Profile profile = new Profile("Name");

        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        habit.setId("XYZ");

        HabitEvent old = new HabitEvent("XYZ", new Date());
        old.setId("TEST");
        habit.addHabitEvent(old);

        HabitEvent newEvent = new HabitEvent("DBZ", new Date());
        newEvent.setId("TEST");
        OfflineEvent event = new EditHabitEvent(newEvent);

        profile.addHabit(habit);

        profile.tryHabitEvent(event);
        assertFalse(old.getComment().equals(newEvent.getComment()));
        profile.synchronize();
        assertTrue(old.getComment().equals(newEvent.getComment()));
    }

    @Test
    public void testOfflineAddEvent(){
        Profile profile = new Profile("Name");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        OfflineEvent event = new AddHabitEvent("Habit", old);
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.getHabits().get(0).setId("Habit");

        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 0);
        profile.tryHabitEvent(event);
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 0);
        profile.synchronize();
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
    }

    @Test
    public void testOfflineDeleteEvent(){
        Profile profile = new Profile("45a");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        old.setId("XYZ");
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.getHabits().get(0).setId("Habit");
        profile.getHabits().get(0).addHabitEvent(old);

        HabitEvent toDelete = new HabitEvent("XYZ", new Date());
        toDelete.setId("TOO");
        OfflineEvent event = new DeleteHabitEvent(toDelete);

        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
        profile.tryHabitEvent(event);   // not deleted yet since offline
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
        profile.synchronize();
        // different IDs, so does not get deleted
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);

        toDelete.setId("XYZ");
        event = new DeleteHabitEvent(toDelete);
        profile.tryHabitEvent(event);   // not deleted yet since offline
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
        profile.synchronize();
        // same IDs, should delete now
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 0);

    }

}
