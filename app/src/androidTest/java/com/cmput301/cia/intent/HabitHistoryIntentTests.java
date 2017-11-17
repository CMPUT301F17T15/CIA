/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.FilterEventsActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for the habit history
 * NOTE: These tests require an internet connection
 */

public class HabitHistoryIntentTests extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public HabitHistoryIntentTests(){
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        // login and navigate to habit history
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
    }

    public void testNavigation()throws Exception{
        // test current activity
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
        // test return button works?
        solo.clickOnButton("Return");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);

        // test habits button words?
        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);
    }

    public void testCheck()throws Exception{
        // test can check works?
        assertEquals("Filter is Selected",false, solo.isCheckBoxChecked("Type"));
        // if click?
        solo.clickOnCheckBox(0);
        solo.sleep(300);
        assertEquals("Filter is not Selected",true, solo.isCheckBoxChecked("Type"));

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

        // make sure all displayed events are now of the same type as the filter habit
        events = (List<HabitEvent>) method.invoke(solo.getCurrentActivity());
        for (HabitEvent event : events){
            assertTrue(event.getHabitId().equals(filter.getId()));
        }

        solo.clickOnCheckBox(0);
        solo.sleep(300);

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
                assertTrue(event.getDate().before(previousDate));
            previousDate = event.getDate();
        }

    }
}
