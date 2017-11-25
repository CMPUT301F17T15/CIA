/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.events.FilterEventsActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 24 2017
 *
 * This class tests the UI for the habit history
 */

public class HabitHistoryIntentTests extends ActivityInstrumentationTestCase2<HistoryActivity> {
    private Solo solo;

    public HabitHistoryIntentTests(){
        super(HistoryActivity.class);
    }

    public void setUp() throws Exception{
        Profile profile = new TestProfile("xyz");
        Habit habit = new Habit("T1", "", new Date(), new ArrayList<Integer>(), "");
        habit.setId("one");
        habit.addHabitEvent(new HabitEvent(""));

        Calendar calendar = new GregorianCalendar();
        calendar.set(1991, 10, 10);

        habit.addHabitEvent(new HabitEvent("test", calendar.getTime()));
        habit.addHabitEvent(new HabitEvent(""));
        habit.getEvents().get(0).setId("two");
        habit.getEvents().get(1).setId("two");
        profile.addHabit(habit);

        Habit habit2 = new Habit("X95", "", new Date(), new ArrayList<Integer>(), "");
        habit2.setId("h2");
        habit2.addHabitEvent(new HabitEvent("", calendar.getTime()));
        habit2.addHabitEvent(new HabitEvent("test"));
        habit2.getEvents().get(0).setId("three");
        habit2.getEvents().get(1).setId("four");
        profile.addHabit(habit2);

        Intent intent = new Intent();
        intent.putExtra(HistoryActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testNavigation() throws Exception {
        solo.clickOnButton("Habits");
        solo.sleep(2000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);
    }

    public void testFilters() throws Exception {

        solo.clickOnCheckBox(0);

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("historyList");
        field.setAccessible(true);
        ListView listView = (ListView)field.get(solo.getCurrentActivity());
        int oldSize = listView.getAdapter().getCount();

        // make sure that nothing is filtered, because no habit was selected and no filter text was chosen
        solo.clickOnButton("Filter");
        solo.sleep(600);
        assertTrue(oldSize == listView.getAdapter().getCount());

        // filter text chosen, so filter by that since no habit selected
        solo.enterText(0, "test");
        Method method = solo.getCurrentActivity().getClass().getDeclaredMethod("getDisplayedEvents");
        method.setAccessible(true);
        List<HabitEvent> events = (List<HabitEvent>) method.invoke(solo.getCurrentActivity());
        for (HabitEvent event : events){
            assertTrue(event.getComment().contains("test"));
        }
        // the user has 2 habit events containing test as the comment
        assertTrue(events.size() == 2);

        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);
        solo.clickInList(1, 0);
        solo.sleep(1000);
        solo.clickOnButton("Finish");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        Field habitField = solo.getCurrentActivity().getClass().getDeclaredField("filterHabit");
        habitField.setAccessible(true);
        Habit filter = (Habit)habitField.get(solo.getCurrentActivity());
        assertTrue(filter != null);

        solo.clickOnButton("Filter");
        solo.sleep(1000);

        // make sure all displayed events are now of the same type as the filter habit
        events = (List<HabitEvent>) method.invoke(solo.getCurrentActivity());
        for (HabitEvent event : events){
            assertTrue(event.getHabitId().equals(filter.getId()));
        }

        solo.clickOnCheckBox(0);
        solo.sleep(600);

        solo.clickOnButton("Filter");
        solo.sleep(1000);

        // now filtering by text again
        events = (List<HabitEvent>) method.invoke(solo.getCurrentActivity());
        for (HabitEvent event : events){
            assertTrue(event.getComment().contains("test"));
        }

    }

    public void testEditText()throws Exception{
        // try to enter the text
        solo.clickOnEditText(0);
        String strInput = "Sample code 1";
        solo.sleep(300);
        solo.enterText(0, strInput);
        solo.sleep(300);
        boolean actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
        // clear all text
        solo.clearEditText(0);
        solo.sleep(300);
        strInput = "Sample code 2";
        solo.typeText(0, strInput);
        actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
    }

    /**
     * Test to make sure events are in descending order of date
     */
    public void testOrdering() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = solo.getCurrentActivity().getClass().getDeclaredMethod("getDisplayedEvents");
        method.setAccessible(true);
        List<HabitEvent> events = (List<HabitEvent>) method.invoke(solo.getCurrentActivity());

        // the date the previous event was on
        Date previousDate = null;

        for (HabitEvent event : events){
            // make sure this event was earlier, since it is in descending order
            if (previousDate != null)
                assertFalse(previousDate.before(event.getDate()));
            previousDate = event.getDate();
        }

    }
}
