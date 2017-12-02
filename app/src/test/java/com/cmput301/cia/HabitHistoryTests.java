/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Guanfang, Adil Malik
 * @version 2
 * Date: Dec 02, 2017
 *
 * Unit tests for the habit history aspect of profiles
 */

@RunWith(JUnit4.class)
public class HabitHistoryTests {

    /** testing method(s): Part of ViewHabitHistory, It will
     * test will a new profile has been created.
     */
    @Test
    public void testProfile(){
        // create a new habit
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String name = "Test1";
        // create a new profile
        Profile profile = new Profile(name);
        //  We put habit to the new profile.
        profile.addHabit(habit);
        // We see is there any profile.
        assertNotNull(profile);
    }

    /** testing method(s): Part of ViewHabitHistory, It will
     * test will a new habit list has been created
     */
    @Test
    public void testCollectHabits(){
        // We create new habit,
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String name = "Test1";
        // We create a new profile,
        Profile profile = new Profile(name);
        profile.addHabit(habit);
        // We see there is any habits,
        List<Habit>habitList=profile.getHabits();
        assertNotNull(habitList);
    }

    /** testing method(s): Part of ViewHabitHistory(), It will
     * test can we find the missing date?
     */
    @Test
    public void testFindMissing(){
        //We create a new habit.
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3,4,5,6,7);
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

    /** testing method(s): part of HabitFilterByType(), It will
     * test can we filter habits by time?
     */
    @Test
    public void testSort(){
        // We create two new habits.
        String title = "Habit1";
        String reason = "Reason1";
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, new Date(), days, "");
        String title2 = "Habit2";
        String reason2 = "Reason2";
        // We set one is earlier than another

        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, new Date(), days2, "");
        String name = "Test1";
        Profile profile = new Profile(name);
        // We add them to the new profile.
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<CompletedEventDisplay> habitList = profile.getHabitHistory();
        assertTrue(habitList.size() == 0);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1856, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz", calendar.getTime()));
        calendar.set(2105, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz2", calendar.getTime()));
        calendar.set(2085, 1, 9);
        habit.addHabitEvent(new HabitEvent("xyz21", calendar.getTime()));
        habit.addHabitEvent(new HabitEvent("xyz1"));
        habit2.addHabitEvent(new HabitEvent("1"));
        habitList = profile.getHabitHistory();

        // make sure ordering is in descending based on date
        assertTrue(habitList.get(0).getCompletionDate().getTime() > habitList.get(1).getCompletionDate().getTime());
    }

    /** testing method(s): part of HabitFilterByType(), It will
     * test can we filter habits by type?
     */
    @Test
    public void testFilterByType(){
        // We create two new habits.
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String title2 = "Habit2";
        String reason2 = "Reason2";
        Date date2 = new Date();
        date2.setYear(1856);
        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, date2, days2, "");
        // We set types,
        habit.setType("1");
        habit2.setType("2");
        String name = "Test1";
        // We add them to the new profile,
        Profile profile = new Profile(name);
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<CompletedEventDisplay> habitList = profile.getHabitHistory(habit);
        // We assert there is true or not.
        assertTrue(habitList.size() == 0);

        Date date3 = new Date();
        date3.setYear(2105);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1856, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz", calendar.getTime()));
        calendar.set(2105, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz2", calendar.getTime()));
        calendar.set(2085, 1, 9);
        habit.addHabitEvent(new HabitEvent("xyz21", calendar.getTime()));
        habit.addHabitEvent(new HabitEvent("xyz1"));
        habit2.addHabitEvent(new HabitEvent("1"));

        habitList = profile.getHabitHistory(habit);
        assertTrue(habitList.size() == 4);
        assertTrue(profile.getHabitHistory(habit2).size() == 1);
        assertTrue(habitList.get(0).getCompletionDate().getTime() > habitList.get(1).getCompletionDate().getTime());
    }

    /** testing method(s): part of HabitFilterByType(), It will
     * test can we filter habits by comment?
     */
    @Test
    public void testFilterByComment(){
        //We create two new habits,
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days, "");
        String title2 = "Habit2";
        String reason2 = "Reason2";
        Date date2 = new Date();
        date2.setYear(2100);
        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, date2, days2, "");
        // We set comments
        habit.setReason("1");
        habit2.setReason("2");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1856, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz", calendar.getTime()));
        calendar.set(2105, 1, 1);
        habit.addHabitEvent(new HabitEvent("xyz2", calendar.getTime()));
        calendar.set(2085, 1, 9);
        habit.addHabitEvent(new HabitEvent("xyz21", calendar.getTime()));
        habit.addHabitEvent(new HabitEvent("xyz1"));
        habit2.addHabitEvent(new HabitEvent("1"));
        String name = "Test1";
        Profile profile = new Profile(name);
        // We add to new profile,
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<CompletedEventDisplay> habitList = profile.getHabitHistory("1");
        assertTrue(habitList.size() == 3);
        assertTrue(profile.getHabitHistory("o").size() == 0);
        assertTrue(habitList.get(0).getCompletionDate().getTime() > habitList.get(1).getCompletionDate().getTime());
    }

}
