/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.unit;

import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.cmput301.cia.TestProfile;
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tinghui, Adil Malik
 * @version 3
 * Date: Nov 24 2017
 *
 * Unit tests for the profile class
 * NOTE: These tests require an internet connection or they will fail
 */

// TODO: test .follow and .unfollow

@RunWith(AndroidJUnit4.class)
public class ProfileTests {

    /**
     * following
     */
    @Test
    public void testAddRequest() throws InterruptedException {
        Map<String, String> values = new HashMap<>();
        values.put("name", "blah");
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", profile != null);
        values.put("name", "test");
        Profile request = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", request != null);

        // reset profiles from previous runs
        profile.removeFollowRequest(request);
        Thread.sleep(1000);
        request.removeFollowRequest(profile);
        Thread.sleep(1000);
        profile.unfollow(request);
        Thread.sleep(1000);
        request.unfollow(profile);
        Thread.sleep(1000);

        profile.addFollowRequest(request);
        Thread.sleep(1000);

        // make aure a follow request was added
        assertTrue(profile.getFollowRequests().contains(request));
        assertFalse(request.getFollowRequests().contains(profile));

        assertTrue(profile.hasFollowRequest(request));
        assertFalse(request.hasFollowRequest(profile));
        assertFalse(profile.isFollowing(request));
        assertFalse(request.isFollowing(profile));
    }

    @Test
    public void testRemoveRequest() throws InterruptedException {
        Map<String, String> values = new HashMap<>();
        values.put("name", "blah");
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", profile != null);
        values.put("name", "test");
        Profile request = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", request != null);

        // reset profiles from previous runs
        profile.removeFollowRequest(request);
        Thread.sleep(1000);
        request.removeFollowRequest(profile);
        Thread.sleep(1000);
        profile.unfollow(request);
        Thread.sleep(1000);
        request.unfollow(profile);
        Thread.sleep(1000);

        profile.addFollowRequest(request);
        Thread.sleep(1000);
        profile.removeFollowRequest(request);
        Thread.sleep(1000);

        assertFalse(profile.hasFollowRequest(request));
        assertFalse(profile.isFollowing(request));
        assertFalse(request.isFollowing(profile));

        // make aure follow request was removed
        assertTrue(!profile.getFollowRequests().contains(request));
        assertTrue(!request.getFollowRequests().contains(profile));
    }

    @Test
    public void testFollowing() throws InterruptedException {
        Map<String, String> values = new HashMap<>();
        values.put("name", "blah");
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", profile != null);
        values.put("name", "test");
        Profile request = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", request != null);

        // reset profiles from previous runs
        profile.removeFollowRequest(request);
        Thread.sleep(1000);
        request.removeFollowRequest(profile);
        Thread.sleep(1000);
        profile.unfollow(request);
        Thread.sleep(1000);
        request.unfollow(profile);
        Thread.sleep(1000);

        profile.addFollowRequest(request);
        Thread.sleep(1000);
        profile.acceptFollowRequest(request);
        Thread.sleep(1000);

        // remove the request
        assertFalse(profile.hasFollowRequest(request));
        assertFalse(request.hasFollowRequest(profile));

        // request is following profile, but not the other way around
        assertTrue(request.isFollowing(profile));
        assertFalse(profile.isFollowing(request));

        request.addFollowRequest(profile);
        Thread.sleep(1000);
        request.acceptFollowRequest(profile);
        Thread.sleep(1000);

        // remove the request
        assertFalse(profile.hasFollowRequest(request));
        assertFalse(request.hasFollowRequest(profile));

        // now both are following each other
        assertTrue(request.isFollowing(profile));
        assertTrue(profile.isFollowing(request));
    }

    @Test
    public void testFollowedHistory() throws InterruptedException {
        Map<String, String> values = new HashMap<>();
        values.put("name", "blah");
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", profile != null);
        values.put("name", "test");
        Profile request = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values).first;
        assertTrue("database error", request != null);

        // reset profiles from previous runs
        profile.removeFollowRequest(request);
        Thread.sleep(1000);
        request.removeFollowRequest(profile);
        Thread.sleep(1000);
        profile.unfollow(request);
        Thread.sleep(1000);
        request.unfollow(profile);
        Thread.sleep(1000);

        profile.addFollowRequest(request);
        Thread.sleep(1000);
        profile.acceptFollowRequest(request);
        Thread.sleep(1000);

        List<CompletedEventDisplay> eventList = request.getFollowedHabitHistory();

        // only the most recent event should be visible to the follower, so count how much that should be
        int correctSize = 0;
        for (Habit habit : profile.getHabits()){
            if (habit.getLastCompletionDate() != null)
                ++correctSize;
        }

        assertTrue(eventList.size() >= correctSize);

        profile.addHabit(new Habit("ProfileTests Habit", "", new Date(), new ArrayList<Integer>(), "test"));
        profile.getHabits().get(profile.getHabitsCount() - 1).addHabitEvent(new HabitEvent("XYZ"));

        assertTrue(profile.save());
        Thread.sleep(2000);

        eventList = request.getFollowedHabitHistory();

        // a new event is added, so the list size should increase by 1
        assertTrue(eventList.size() == correctSize + 1);
        //assertTrue(request.getHabitHistory().size() == 0);

        // still the same size, because the new event is now the most recent one
        profile.getHabits().get(profile.getHabitsCount() - 1).addHabitEvent(new HabitEvent("XYZ"));

        assertTrue(profile.save());
        Thread.sleep(2000);

        eventList = request.getFollowedHabitHistory();

        assertTrue(eventList.size() >= correctSize + 1);
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

        assertTrue(profile.getPowerPoints() == 0);
    }

    @Test
    public void testFollowedHabits() throws InterruptedException {

        Map<String, String> searchTerms = new HashMap<>();
        searchTerms.put("name", "nowitenz3");
        Pair<Profile, Boolean> pair1 = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);
        assertTrue(pair1.first != null && pair1.second);        // if this fails then there was a connection error

        searchTerms.put("name", "test");
        Pair<Profile, Boolean> pair2 = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);
        assertTrue(pair2.first != null && pair2.second);        // if this fails then there was a connection error

        searchTerms.put("name", "blah");
        Pair<Profile, Boolean> pair3 = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);
        assertTrue(pair3.first != null && pair3.second);        // if this fails then there was a connection error

        Profile profile = pair1.first;
        Profile request = pair2.first;
        Profile profile2 = pair3.first;

        // some habits were made just for testing purposes, and don't have valid elasticsearch IDs, so remove them
        Profile[] profiles = new Profile[]{profile, request, profile2};
        for (Profile prof : profiles) {
            Iterator<Habit> habit = prof.getHabits().iterator();
            while (habit.hasNext()){
                if (habit.next().getId().length() < 10){
                    habit.remove();
                }
            }
        }

        profile.addFollowRequest(request);
        Thread.sleep(700);
        profile.acceptFollowRequest(request);
        Thread.sleep(700);
        profile2.addFollowRequest(request);
        Thread.sleep(700);
        profile2.acceptFollowRequest(request);
        Thread.sleep(700);

        // request is now following profile and profile2
        List<Profile> reqFollowing = request.getFollowing();
        assertTrue(reqFollowing.contains(profile) && reqFollowing.contains(profile2));

        profile.addHabit(new Habit("X", "", new Date(), Arrays.asList(1,2,3), ""));
        profile.addHabit(new Habit("A", "", new Date(), Arrays.asList(1,2,3), ""));
        profile.addHabit(new Habit("292", "", new Date(), Arrays.asList(1,2,3), ""));
        profile.save();
        Thread.sleep(2000);

        List<Habit> habits = request.getFollowedHabits();
        // 3 habits were just added, so the size should always be 3
        assertTrue(habits.size() >= 3);

        // Verify that all of the habits are ordered by {creator name, habit title}

        // name of the previous habit's creator
        String previousName = null;
        // the previous habit's title
        String previousTitle = null;

        List<Profile> followed = request.getFollowing();

        for (Habit habit : habits){
            String name = null;

            // get the name of the user who made this habit
            for (Profile p : followed){
                if (p.getHabitById(habit.getId()) != null){
                    name = p.getName();
                    break;
                }
            }

            // a profile should be found that contains this habit
            assertTrue(name != null);

            if (previousName != null) {
                // if both names are the same, then the previous habit's title must be lexicographically smaller (or equivalent if same name)
                if (previousName.compareTo(name) == 0){
                    assertTrue(previousTitle.compareTo(habit.getTitle()) <= 0);
                } else {
                    // names are different, so previous name must be lexicographically smaller since names are unique
                    assertTrue(previousName.compareTo(name) < 0);
                }
            }

            previousName = name;
            previousTitle = habit.getTitle();
        }

    }

}
