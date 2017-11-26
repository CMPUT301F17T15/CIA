/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.OfflineUnitTests;
import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.activities.events.HabitEventViewActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Version 3
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for creating habit events
 * NOTE: These tests require an internet connection
 */

public class CreateHabitEventIntentTests extends ActivityInstrumentationTestCase2<HomePageActivity> {

    private Solo solo;

    public CreateHabitEventIntentTests() {
        super(com.cmput301.cia.activities.HomePageActivity.class);
    }

    public void setUp() throws Exception{

        List<Integer> allDays = new ArrayList<>();
        for (int i = 1; i <= 7; ++i){
            allDays.add(i);
        }

        Profile profile = new TestProfile("xyz");
        Habit habit = new Habit("T1", "", new Date(), allDays, "");
        habit.setId("one");
        profile.addHabit(habit);

        Habit habit2 = new Habit("T55", "", new Date(), allDays, "");
        habit2.setId("h2");
        profile.addHabit(habit2);

        Intent intent = new Intent();
        intent.putExtra(HomePageActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

    }

    public void testCommentLength() throws NoSuchFieldException, IllegalAccessException {

        solo.clickInList(1, 1);
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.enterText((EditText)solo.getView(R.id.cheCommentEditText), "@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD");
        // max length = 20
        assertFalse(solo.waitForText("@@@@@@@@@@@@@@@@@@Y@@WDALOAWDAOWD", 1, 2000));
        assertTrue(((EditText) solo.getView(R.id.cheCommentEditText)).getText().toString().length() == 20);
    }

    public void testFinish() throws NoSuchFieldException, IllegalAccessException {

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());
        int habitEvents = user.getHabitHistory().size();

        // Select the 1st option in the second list (the "today's tasks" list)
        solo.clickInList(1, 1);
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Save");

        solo.sleep(6000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // make sure no new event activity is started
        solo.clickInList(1, 1);
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // assert that a new event was added
        assertTrue(user.getHabitHistory().size() == habitEvents + 1);
    }

    public void testCancel() throws NoSuchFieldException, IllegalAccessException {
        solo.clickInList(1, 1);
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.goBack();
        //solo.clickOnButton("Cancel");
        solo.sleep(3000);

        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());
        int habitEvents = user.getHabitHistory().size();

        solo.clickInList(1, 1);
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.goBack();
        //solo.clickOnButton("Cancel");
        solo.sleep(3000);

        // assert new habit events count is equal to old
        assertTrue(user.getHabitHistory().size() == habitEvents);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}