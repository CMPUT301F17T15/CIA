/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.robotium.solo.Solo;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * This class tests the UI for the features on the home page of the activity
 */

// TODO

public class HomePageIntentTests extends ActivityInstrumentationTestCase2<HomePageActivity> {

    private Solo solo;

    public HomePageIntentTests() {
        super(com.cmput301.cia.activities.HomePageActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testNavigation(){
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        solo.clickOnMenuItem("Add New Habit");
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
    }

    public void testCompleteHabit(){

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}