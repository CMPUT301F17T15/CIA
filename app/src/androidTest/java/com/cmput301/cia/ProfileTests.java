/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.support.test.runner.AndroidJUnit4;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by WTH on 2017-10-22.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileTests {

    /**
     * following
     */
    @Test
    public void testAddRequest(){
        Profile profile = new TestProfile("name");
        Profile request = new TestProfile("Mike");
        profile.addFollowRequest(request);
        assertTrue(profile.hasFollowRequest(request));
        assertFalse(request.hasFollowRequest(profile));
    }

    @Test
    public void testRemoveRequest(){
        Profile profile = new TestProfile("name");
        Profile request = new TestProfile("Mike");
        profile.addFollowRequest(request);
        profile.removeFollowRequest(request);
        assertFalse(profile.hasFollowRequest(request));
    }

    @Test
    public void testFollowing(){
        Profile profile = new TestProfile("name");
        Profile request = new TestProfile("Mike");
        profile.addFollowRequest(request);
        profile.acceptFollowRequest(request);
        assertFalse(profile.hasFollowRequest(request));
        assertTrue(request.isFollowing(profile));
        assertFalse(request.hasFollowRequest(profile));
    }

    @Test
    public void testFollowedHistory(){
        Profile profile = new TestProfile("name");
        Profile request = new TestProfile("Mike");
        profile.addFollowRequest(request);
        profile.acceptFollowRequest(request);
        List<HabitEvent> eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 0);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>(), ""));
        assertTrue(request.getFollowedHabitHistory().size() == 0);
        profile.getHabits().get(0).addHabitEvent(new HabitEvent("XYZ"));
        eventList = request.getFollowedHabitHistory();
        assertTrue(eventList.size() == 1);
        assertTrue(profile.getFollowedHabitHistory().size() == 0);
    }

    @Test
    public void testCategories(){

        Profile profile = new TestProfile("N");

        // no categories to start off with
        assertTrue(profile.getHabitCategories().size() == 0);

        // one category
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>(), "dx1"));
        assertTrue(profile.getHabitCategories().size() == 1);
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>(), "dx1"));
        assertTrue(profile.getHabitCategories().size() == 1);

        // a new category
        profile.addHabit(new Habit("XTZ", "", new Date(), new ArrayList<Integer>(), "dx2"));
        assertTrue(profile.getHabitCategories().size() == 2);

        assertTrue(profile.getHabitsInCategory("dx1").size() == 2);
        assertTrue(profile.getHabitsInCategory("dx2").size() == 1);

    }

    @Test
    public void testOnDayEnd(){

        Profile profile = new TestProfile("NewProfile");

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 7; ++i)
            days.add(i);

        GregorianCalendar testcal = new GregorianCalendar();
        testcal.set(2017, 10, 4);
        profile.setLastLogin(testcal.getTime());

        testcal.set(2017, 10, 11);
        Date currentDate = testcal.getTime();

        profile.addHabit(new Habit("StartToday", "Reason", currentDate, days, ""));

        testcal.set(2017, 10, 7);
        profile.addHabit(new Habit("StartBefore", "Reason", testcal.getTime(), days, ""));

        profile.addHabit(new Habit("CompletedOnce", "Reason", testcal.getTime(), days, ""));
        List<Habit> habits = profile.getHabits();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(profile.getLastLogin());
        // go through each date between the user's last login and the current date
        while (!DateUtilities.isSameDay(calendar.getTime(), currentDate)){
            // update all events at the end of that date, to make sure they are marked as missed if they weren't completed
            // on that day
            profile.onDayEnd(calendar.getTime());
            calendar.add(Calendar.DATE, 1);

            // complete it on the 9th
            int newDay = calendar.get(Calendar.DATE);
            if (newDay == 9){
                habits.get(2).addHabitEvent(new HabitEvent("XYZ", calendar.getTime()));
            }
        }

        // started today, but today is not over. Should not count as missed
        assertTrue(habits.get(0).getTimesMissed() == 0);

        // miss it on the 7th, 8th, 9th, 10th
        assertTrue(habits.get(1).getTimesMissed() == 4);

        // miss it on the 7th, 8th, 10th
        assertTrue(habits.get(2).getTimesMissed() == 3);

    }

}
