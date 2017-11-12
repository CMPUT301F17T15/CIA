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
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.UserProfileActivity;
import com.robotium.solo.Solo;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * This class tests the UI for the features on the home page of the activity
 */

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

        // select profile option in menu
        solo.clickOnActionBarItem(R.id.menu_button_My_Profile);
        solo.clickOnMenuItem("My Profile");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);
        solo.goBackToActivity("HomePageActivity");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // select add new habit
        solo.clickOnActionBarItem(R.id.menu_button_Add_New_Habit);
        solo.clickOnMenuItem("Add New Habit");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);
        solo.goBackToActivity("HomePageActivity");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // select habit history
        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
        solo.clickOnButton("Return");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // TODO: power rankings

        // TODO: overall rankings

        // TODO: followed users

        // TODO: whatever else is in the menu bar

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}