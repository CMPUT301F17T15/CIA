/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.app.Application;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Shipin1 on 2017-10-20.
 * Version 1.1
 * Last Modified on 2017-10-21
 *
 * This is the unit testing class for class Habit.
 * All the methods and constructor implemented in class Habit has been tested here.
 */

public class HabitUnitTests extends ActivityInstrumentationTestCase2 {

    public HabitUnitTests(){
        super(MainActivity.class);
    }

    public void testConstructor(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertNotNull(habit);
    }

    public void testGetDaysOfWeek(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.getDaysOfWeek() == days);
    }

    public void testSetDaysOfWeek(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        List<Integer> newDays = Arrays.asList(1,2,3,4);
        Habit habit = new Habit(title, reason, date, days);
        habit.setDaysOfWeek(newDays);
        assertTrue(habit.getDaysOfWeek() == newDays);
    }

    public void testGetHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        assertTrue(habit.getEvents().isEmpty());
        habit.getEvents().add(event);
        assertFalse(habit.getEvents().isEmpty());
    }

    public void testAddHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        assertTrue(habit.getEvents().isEmpty());
        habit.addHabitEvent(event);
        assertTrue(habit.getEvents().contains(event));
    }

    public void testRemoveHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        habit.addHabitEvent(event);
        assertFalse(habit.getEvents().isEmpty());
        habit.removeHabitEvent(event);
        assertTrue(habit.getEvents().isEmpty());
    }

    public void testGetTitle(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.getTitle() == title);
    }

    public void testSetTitle(){
        String title = "Habit1";
        String newTitle = "Habit2";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        habit.setTitle(newTitle);
        assertTrue(habit.getTitle() == newTitle);
    }

    public void testSetReason(){
        String title = "Habit1";
        String newReason = "Reason2";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        habit.setReason(newReason);
        assertTrue(habit.getReason() == newReason);
    }

    public void testGetReason(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.getReason() == reason);
    }

    public void testGetStartDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.getStartDate() == date);
    }

    public void testSetStartDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        Date newDate = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(date != newDate);
        habit.setStartDate(newDate);
        assertTrue(habit.getStartDate() == newDate);
    }

    public void testOccursOn(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.occursOn(1) == true);
        assertTrue(habit.occursOn(2) == true);
        assertTrue(habit.occursOn(3) == true);
        assertFalse(habit.occursOn(4) == true);
        assertFalse(habit.occursOn(5) == true);
    }

    public void testGetTimesMissed(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        Date missDate = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        assertTrue(habit.getTimesMissed() == 0);
        assertNotSame(date, missDate);
        habit.miss(missDate);
        habit.miss(date);
        assertTrue(habit.getTimesMissed() == 2);
    }

    public void testGetTimesCompleted(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        habit.addHabitEvent(event);
        assertTrue(habit.getTimesCompleted() == 1);
    }

    public void testGetMostRecentEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment1 = "Event1";
        HabitEvent event1 = new HabitEvent(comment1);
        habit.addHabitEvent(event1);
        String comment2 = "Event2";
        HabitEvent event2 = new HabitEvent(comment2);
        habit.addHabitEvent(event2);
        assertEquals(habit.getMostRecentEvent(), event2);
    }

    public void testGetLastCompletionDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String comment1 = "Event1";
        HabitEvent event1 = new HabitEvent(comment1, date);
        habit.addHabitEvent(event1);
        assertTrue(habit.getLastCompletionDate() == date);
        /**
         * Using "date" here just for testing purposes.
         */
    }

    public void testGetType(){
        String type = "type1";
        String title = "Habit1";
        String reason = "Reason1";
        String comment = "Habit comment";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, type);
        assertEquals(habit.getType(), type);
    }

    public void testSetType(){
        String type = "type1";
        String type2 = "type2";
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, type);
        assertEquals(habit.getType(), type);
        habit.setType(type2);
        assertEquals(habit.getType(), type2);
    }

    public void testGetComment(){
        String type = "type1";
        String title = "Habit1";
        String reason = "Reason1";
        String comment = "Habit comment";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, type);
        assertEquals(habit.getReason(), comment);
    }

    public void testSetComment(){
        String type = "type1";
        String title = "Habit1";
        String reason = "Reason1";
        String comment1 = "Habit comment1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, type);
        habit.setReason(comment1);
        assertEquals(habit.getReason(), comment1);
    }

}
