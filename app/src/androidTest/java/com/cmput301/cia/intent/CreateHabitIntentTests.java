/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for creating habits
 * NOTE: These tests require an internet connection
 */

public class CreateHabitIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public CreateHabitIntentTests(){super(com.cmput301.cia.activities.MainActivity.class);}

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
    }

    public void testClear(){
        solo.clickOnActionBarItem(R.id.CreateNewHabitButton);
        solo.clickOnMenuItem("Add New Habit");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        solo.enterText((EditText)solo.getView(R.id.reason), "reason");
        solo.sleep(500);
        solo.enterText((EditText)solo.getView(R.id.habitName), "none");
        solo.sleep(500);

        solo.clickOnButton("Clear");
        solo.sleep(1000);
        assertTrue(solo.getEditText(1).getText().toString().equals(""));
        assertTrue(solo.getEditText(0).getText().toString().equals(""));
    }

    public void testAdd() throws NoSuchFieldException, IllegalAccessException {

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        int count = ((Profile)field.get(solo.getCurrentActivity())).getHabitsCount();

        solo.clickOnActionBarItem(R.id.CreateNewHabitButton);
        solo.clickOnMenuItem("Add New Habit");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        solo.enterText((EditText)solo.getView(R.id.reason), "reason");
        solo.sleep(500);
        solo.pressSpinnerItem(0, 1);
        solo.sleep(500);
        solo.clickOnButton("Save");
        solo.sleep(1000);

        // should not change because no days selected
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        // select wednesday
        solo.clickOnView(solo.getView(R.id.day_picker));
        solo.sleep(600);

        solo.clickOnButton("Save");
        solo.sleep(1000);
        // should not change because no title
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        solo.enterText((EditText)solo.getView(R.id.habitName), "nametest");
        solo.sleep(800);

        solo.clickOnButton("Save");
        solo.sleep(4000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // make sure a new habit was added to the user
        assertTrue(((Profile)field.get(solo.getCurrentActivity())).getHabitsCount() == count+1);
    }

    public void testLength(){
        solo.clickOnActionBarItem(R.id.CreateNewHabitButton);
        solo.clickOnMenuItem("Add New Habit");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        // max reason length = 30
        solo.enterText((EditText)solo.getView(R.id.reason), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        solo.sleep(500);
        assertTrue(((EditText)solo.getView(R.id.reason)).getText().toString().length() == 30);

        // max name length = 20
        solo.enterText((EditText)solo.getView(R.id.habitName), "ffffffffffffffffffffffffffffffffffffffffffffffffff");
        solo.sleep(500);
        assertTrue(((EditText)solo.getView(R.id.habitName)).getText().toString().length() == 20);

    }

}
