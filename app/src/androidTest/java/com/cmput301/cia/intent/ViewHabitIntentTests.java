/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomeTabbedActivity;
import com.cmput301.cia.activities.habits.HabitViewActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for viewing and deleting habits
 * NOTE: These tests require an internet connection
 */

public class ViewHabitIntentTests extends ActivityInstrumentationTestCase2<HomeTabbedActivity> {
    private Solo solo;

    public ViewHabitIntentTests(){super(com.cmput301.cia.activities.HomeTabbedActivity.class);}

    public void setUp() throws Exception {

        Profile profile = new TestProfile("xyz");
        Habit habit = new Habit("T1", "", new Date(), Arrays.asList(1, 2, 3), "x");
        habit.setId("one");
        habit.addHabitEvent(new HabitEvent(""));
        habit.getEvents().get(0).setId("two");

        profile.addHabit(habit);
        Habit habit2 = new Habit("T2", "", new Date(), Arrays.asList(1, 2, 3), "y");
        habit2.setId("two");
        profile.addHabit(habit2);

        Intent intent = new Intent();
        intent.putExtra(HomeTabbedActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * Select the first habit in the user's list
     * @return the name of the first habit
     */
    private String loadHabit(){
        // expand the first category
        solo.clickOnView(((ExpandableListView)solo.getView(R.id.HabitTypeExpandableListView)).getAdapter().getView(0, null, null));
        solo.sleep(600);

        String habitName = (String) ((ExpandableListView)solo.getView(R.id.HabitTypeExpandableListView)).getAdapter().getItem(1);

        // click in the first habit in the category
        solo.clickInList(2, 0);
        solo.sleep(1500);

        solo.assertCurrentActivity("wrong activity", HabitViewActivity.class);
        return habitName;
    }

    public void testView(){
        String name = loadHabit();

        // make sure name is correct
        assertTrue(((TextView)solo.getView(R.id.habitName)).getText().toString().equals(name));
    }

    public void testDelete() throws NoSuchFieldException, IllegalAccessException {

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        int count = ((Profile)field.get(solo.getCurrentActivity())).getHabitsCount();

        loadHabit();

        solo.clickOnButton("Delete");
        solo.sleep(1000);
        // confirm deletion dialog
        solo.clickOnView(solo.getView(android.R.id.button1));
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        // make sure habit was removed
        assertTrue(((Profile)field.get(solo.getCurrentActivity())).getHabitsCount() == count-1);
    }

    public void testEdit(){
        loadHabit();

        // should not save because no days of week selected
        solo.clickOnButton("Save");
        solo.sleep(600);
        solo.assertCurrentActivity("wrong activity", HabitViewActivity.class);

        // select wednesday
        solo.clickOnView(solo.getView(R.id.day_picker));
        solo.sleep(600);

        solo.clearEditText(0);
        solo.sleep(600);

        // should not save because no title
        solo.clickOnButton("Save");
        solo.sleep(600);
        solo.assertCurrentActivity("wrong activity", HabitViewActivity.class);

        solo.enterText(0, "newname");
        solo.sleep(600);

        solo.clearEditText(1);
        solo.sleep(600);
        solo.enterText(1, "newreason");
        solo.sleep(600);

        // successful save
        solo.clickOnButton("Save");
        solo.sleep(4500);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        loadHabit();

        // make sure changes took effect here
        assertTrue(((EditText)solo.getView(R.id.habitName)).getText().toString().equals("newname"));
        assertTrue(((EditText)solo.getView(R.id.habitReason)).getText().toString().equals("newreason"));

        // TODO: wednesday selected
        assertTrue(((MaterialDayPicker)solo.getView(R.id.day_picker)).getText().toString().equals("Wednesday\n"));

        // return to home page
        solo.goBack();
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        // make sure changes took effect in the home page
        String name = loadHabit();
        assertTrue(name.equals("newname"));
        assertTrue(((EditText)solo.getView(R.id.habitName)).getText().toString().equals("newname"));
        assertTrue(((EditText)solo.getView(R.id.habitReason)).getText().toString().equals("newreason"));

        // TODO: wednesday selected
        assertTrue(((MaterialDayPicker)solo.getView(R.id.day_picker)).getText().toString().equals("Wednesday\n"));
    }

}
