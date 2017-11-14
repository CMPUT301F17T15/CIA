/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitActivity;
import com.cmput301.cia.activities.EditHabitActivity;
import com.cmput301.cia.activities.HabitViewActivity;
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.StatisticActivity;
import com.cmput301.cia.activities.StatisticViewActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for viewing statistics about habits
 * NOTE: These tests require an internet connection
 */

public class StatisticsIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public StatisticsIntentTests(){super(com.cmput301.cia.activities.MainActivity.class);}

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // add a habit because the list is empty
        if (((ExpandableListView)solo.getView(R.id.HabitTypeExpandableListView)).getAdapter().getCount() == 0){

            solo.clickOnActionBarItem(R.id.CreateNewHabitButton);
            solo.clickOnMenuItem("Add New Habit");
            solo.sleep(1000);
            solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

            solo.enterText((EditText)solo.getView(R.id.edit_Type_Input), "newtype");
            solo.sleep(600);
            solo.clickOnView(solo.getView(R.id.Ok_Button));
            solo.sleep(600);

            solo.enterText((EditText)solo.getView(R.id.reason), "reason");
            solo.sleep(500);
            solo.pressSpinnerItem(0, -1);
            solo.sleep(500);
            // select wednesday
            solo.clickOnView(solo.getView(R.id.day_picker));
            solo.sleep(600);

            solo.enterText((EditText)solo.getView(R.id.habitName), "nametest");
            solo.sleep(500);

            solo.clickOnButton("Save");
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        }
    }

    public void testView(){
        solo.clickOnActionBarItem(R.id.menu_button_Statistic);
        solo.clickOnMenuItem("Statistics");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", StatisticActivity.class);

        solo.clickInList(1);
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", StatisticViewActivity.class);

    }

}
