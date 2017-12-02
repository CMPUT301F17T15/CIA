/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.unit;

import android.support.test.runner.AndroidJUnit4;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Shipin
 * @version 2
 * Date: Dec 02, 2017
 *
 * This is the unit testing class for class Habit.
 * All the methods and constructor implemented in class Habit has been tested here.
 */


@RunWith(AndroidJUnit4.class)
public class HabitUnitTests {

    @Test
    public void testConstructor(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertNotNull(habit);
    }

    @Test
    public void testGetDaysOfWeek(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.getDaysOfWeek() == days);
    }

    @Test
    public void testSetDaysOfWeek(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        List<Integer> newDays = Arrays.asList(1,2,3,4);
        Habit habit = new Habit(title, reason, date, days, "");
        habit.setDaysOfWeek(newDays);
        assertTrue(habit.getDaysOfWeek() == newDays);
    }

    @Test
    public void testGetHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        assertTrue(habit.getEvents().isEmpty());
        habit.getEvents().add(event);
        assertFalse(habit.getEvents().isEmpty());
    }

    @Test
    public void testAddHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        assertTrue(habit.getEvents().isEmpty());
        habit.addHabitEvent(event);
        assertTrue(habit.getEvents().contains(event));
    }

    @Test
    public void testRemoveHabitEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        habit.addHabitEvent(event);
        assertFalse(habit.getEvents().isEmpty());
        habit.removeHabitEvent(event);
        assertTrue(habit.getEvents().isEmpty());
    }

    @Test
    public void testGetTitle(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.getTitle() == title);
    }

    @Test
    public void testSetTitle(){
        String title = "Habit1";
        String newTitle = "Habit2";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        habit.setTitle(newTitle);
        assertTrue(habit.getTitle() == newTitle);
    }

    @Test
    public void testSetReason(){
        String title = "Habit1";
        String newReason = "Reason2";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        habit.setReason(newReason);
        assertTrue(habit.getReason() == newReason);
    }

    @Test
    public void testGetReason(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.getReason() == reason);
    }

    @Test
    public void testGetStartDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.getStartDate() == date);
    }

    @Test
    public void testSetStartDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        Date newDate = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(date != newDate);
        habit.setStartDate(newDate);
        assertTrue(habit.getStartDate() == newDate);
    }

    @Test
    public void testOccursOn(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.occursOn(1) == true);
        assertTrue(habit.occursOn(2) == true);
        assertTrue(habit.occursOn(3) == true);
        assertFalse(habit.occursOn(4) == true);
        assertFalse(habit.occursOn(5) == true);
    }

    @Test
    public void testGetTimesMissed(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3, 4, 5, 6, 7);
        Habit habit = new Habit(title, reason, date, days, "");
        assertTrue(habit.getMissedDates().size() == 0);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // missed the first day after setting new date
        calendar.add(Calendar.DATE, -1);
        habit.setStartDate(calendar.getTime());
        assertTrue(habit.getMissedDates().size() == 1);

        // miss 2 more days
        calendar.add(Calendar.DATE, -2);
        habit.setStartDate(calendar.getTime());
        assertTrue(habit.getMissedDates().size() == 3);

        // today was not counted in the missed part since it isn't over yet, so still at 3
        habit.addHabitEvent(new HabitEvent("", new Date()));
        assertTrue(habit.getMissedDates().size() == 3);

        // completed on one of the missed dates, so decrease missed amount
        habit.addHabitEvent(new HabitEvent("", calendar.getTime()));
        assertTrue(habit.getMissedDates().size() == 2);

        // no days the habit will occur on, so missed dates should always be 0
        habit.setDaysOfWeek(new ArrayList<Integer>());
        assertTrue(habit.getMissedDates().size() == 0);
    }

    @Test
    public void testGetTimesCompleted(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment = "New event";
        HabitEvent event = new HabitEvent(comment);
        habit.addHabitEvent(event);
        assertTrue(habit.getTimesCompleted() == 1);
    }

    @Test
    public void testGetMostRecentEvent(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment1 = "Event1";
        HabitEvent event1 = new HabitEvent(comment1);
        habit.addHabitEvent(event1);
        String comment2 = "Event2";
        HabitEvent event2 = new HabitEvent(comment2);
        habit.addHabitEvent(event2);
        assertEquals(habit.getMostRecentEvent(), event2);
    }

    @Test
    public void testGetLastCompletionDate(){
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String comment1 = "Event1";
        HabitEvent event1 = new HabitEvent(comment1, date);
        habit.addHabitEvent(event1);
        assertTrue(habit.getLastCompletionDate() == date);
        /**
         * Using "date" here just for testing purposes.
         */
    }

    @Test
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

    @Test
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

    @Test
    public void testCompletionRate(){
        String type = "type1";
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3,4,5,6,7);
        Habit habit = new Habit(title, reason, date, days, type);

        assertTrue(habit.getCompletionPercent().equals("0.00%"));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // missed the first day after setting new date
        calendar.add(Calendar.DATE, -1);
        habit.setStartDate(calendar.getTime());
        assertTrue(habit.getCompletionPercent().equals("0.00%"));
        habit.addHabitEvent(new HabitEvent("", calendar.getTime()));
        assertTrue(habit.getCompletionPercent().equals("50.00%"));

        calendar.add(Calendar.DATE, -1);
        habit.setStartDate(calendar.getTime());
        assertTrue(habit.getCompletionPercent().equals("33.33%"));
    }

}
