/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitEventActivity;
import com.cmput301.cia.activities.HabitEventViewActivity;
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 12 2017
 *
 * This class tests the UI for creating habit events
 * NOTE: These tests require an internet connection
 */

public class CreateHabitEventIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public CreateHabitEventIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        // delete all habit events
        while (((ListView)solo.getView(R.id.historyList)).getAdapter().getCount() > 0) {
            solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
            solo.enterText((EditText)solo.getView(R.id.filterEditText), "");
            //solo.goBack();
            solo.sleep(1000);
            solo.clickInList(1, 0);
            solo.sleep(3000);
            solo.assertCurrentActivity("wrong activity", HabitEventViewActivity.class);
            solo.clickOnButton("Delete");
            solo.sleep(1000);
        }

        solo.sleep(100);
        solo.goBackToActivity("HomePageActivity");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
    }

    public void testCommentLength() throws NoSuchFieldException, IllegalAccessException {

        solo.clickInList(1, 1);
        solo.sleep(1000);
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
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Save");

        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // make sure no new event activity is started
        solo.clickInList(1, 1);
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // assert that a new event was added
        assertTrue(user.getHabitHistory().size() == habitEvents + 1);
    }

    public void testCancel() throws NoSuchFieldException, IllegalAccessException {
        solo.clickInList(1, 1);
        solo.sleep(2000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);
        solo.clickOnButton("Cancel");
        solo.sleep(2000);

        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());
        int habitEvents = user.getHabitHistory().size();

        solo.clickInList(1, 1);
        solo.sleep(2000);
        solo.assertCurrentActivity("wrong activity", CreateHabitEventActivity.class);

        solo.clickOnButton("Cancel");
        solo.sleep(2000);

        // assert new habit events count is equal to old
        assertTrue(user.getHabitHistory().size() == habitEvents);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}