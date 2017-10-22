/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by guanfang on 2017/10/21.
 */

public class HabitHistoryTests extends ActivityInstrumentationTestCase2 {

    public HabitHistoryTests(){super (HistoryActivity.class);}

    public void testProfile(){
        // create a new habit
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String name = "Test1";
        // create a new profile
        Profile profile = new Profile(name);
        //  We put habit to the new profile.
        profile.addHabit(habit);
        // We see is there any profile.
        assertNotNull(profile);
    }

    public void testCollectHabits(){
        // We create new habit,
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String name = "Test1";
        // We create a new profile,
        Profile profile = new Profile(name);
        profile.addHabit(habit);
        // We see there is any habits,
        List<Habit>habitList=profile.getHabits();
        assertNotNull(habitList);
    }

    public void testFindMissing(){
        //We create a new habit.
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        // We give it a missing date.
        habit.miss(date);
        String name = "Test1";
        Profile profile = new Profile(name);
        // We add to the new profile.
        profile.addHabit(habit);
        // We find the missing days.
        List<Date> missDates= habit.getMissedDates();
        // We compared to say there is any missing dates.
        assert(missDates.size() > 0);
    }

    public void testSort(){
        // We create two new habits.
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String title2 = "Habit2";
        String reason2 = "Reason2";
        Date date2 = new Date();
        // We set one is earlier than another
        date2.setYear(1856);
        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, date2, days2);
        String name = "Test1";
        Profile profile = new Profile(name);
        // We add them to the new profile.
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<HabitEvent>habitList = profile.getHabitHistory();
        // We check true or not.
        assertTrue(habitList.get(0).getDate().getTime()>habitList.get(1).getDate().getTime());
    }

    public void testFilterByType(){
        // We create two new habits.
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String title2 = "Habit2";
        String reason2 = "Reason2";
        Date date2 = new Date();
        date2.setYear(1856);
        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, date2, days2);
        // We set types,
        habit.setType("1");
        habit2.setType("2");
        String name = "Test1";
        // We add them to the new profile,
        Profile profile = new Profile(name);
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<HabitEvent> habitList = profile.getHabitHistory(habit);
        // We assert there is true or not.
        assertTrue(habitList.size()==1);
    }

    public void testFilterByComment(){
        //We create two new habits,
        String title = "Habit1";
        String reason = "Reason1";
        Date date = new Date();
        List<Integer> days = Arrays.asList(1,2,3);
        Habit habit = new Habit(title, reason, date, days);
        String title2 = "Habit2";
        String reason2 = "Reason2";
        Date date2 = new Date();
        date2.setYear(1856);
        List<Integer> days2 = Arrays.asList(1,2,3);
        Habit habit2 = new Habit(title2, reason2, date2, days2);
        // We set comments
        habit.setReason("1");
        habit2.setReason("2");
        String name = "Test1";
        Profile profile = new Profile(name);
        // We add to new profile,
        profile.addHabit(habit);
        profile.addHabit(habit2);
        List<HabitEvent> habitList = profile.getHabitHistory("1");
        assertTrue(habitList.size()==1);
    }

}
