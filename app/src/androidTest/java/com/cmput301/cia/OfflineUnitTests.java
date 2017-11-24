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

import static org.junit.Assert.assertTrue;

/**
 * @author Adil Malik
 * @version 3
 * Date: Nov 23 2017
 *
 * Tests the offline events that will be synchronized with the database when the user
 * regains connectivity.
 */

@RunWith(AndroidJUnit4.class)
public class OfflineUnitTests {

    /**
     * A mock profile class used only for testing
     */
    static class TestProfile extends Profile {

        public TestProfile(String name){
            super(name);
        }

        /**
         * Overridden implementation to prevent saving of the test profiles
         * @param event represents the object that handles what needs to be done
         */
        public void tryHabitEvent(OfflineEvent event){
            event.handle(this);
        }

    }

    // TODO: find way to force device offline and then integrate ElasticSearch
    // ex: WifiManager
    @Test
    public void testOfflineEditEvent(){
        Profile profile = new TestProfile("Name");

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
        newEvent.setHabitId("XYZ");
        newEvent.setId("TEST");
        OfflineEvent event = new EditHabitEvent(newEvent);

        profile.addHabit(habit);

        profile.tryHabitEvent(event);
        assertTrue(old.getComment().equals(newEvent.getComment()));
    }

    @Test
    public void testOfflineAddEvent(){
        Profile profile = new TestProfile("Name");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        OfflineEvent event = new AddHabitEvent("Habit", old);
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.getHabits().get(0).setId("Habit");

        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 0);
        profile.tryHabitEvent(event);
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);
    }

    @Test
    public void testOfflineDeleteEvent(){
        Profile profile = new TestProfile("45a");
        HabitEvent old = new HabitEvent("XYZ", new Date());
        old.setId("XYZ");
        old.setHabitId("Habit");
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.addHabit(new Habit("Habit", "", new Date(), new ArrayList<Integer>(), ""));
        profile.getHabits().get(0).setId("Habit");
        profile.getHabits().get(0).addHabitEvent(old);
        profile.getHabits().get(1).setId("Habit2");

        HabitEvent toDelete = new HabitEvent("XYZ", new Date());
        toDelete.setId("XYZ");
        toDelete.setHabitId("Habit2");
        OfflineEvent event = new DeleteHabitEvent(toDelete);

        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);        // not executed yet
        profile.tryHabitEvent(event);
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 1);        // different habit ID, so does not get deleted

        toDelete.setHabitId("Habit");
        event = new DeleteHabitEvent(toDelete);
        profile.tryHabitEvent(event);                                           // deleted now that habit ID is correct
        assertTrue(profile.getHabits().get(0).getTimesCompleted() == 0);

    }

}
