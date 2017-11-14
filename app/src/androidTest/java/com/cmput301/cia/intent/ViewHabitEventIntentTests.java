/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HabitEventViewActivity;
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for viewing habit events
 * NOTE: These tests require an internet connection
 * NOTE: Delete habit event is already tested in setUp() for CreateHabitEventIntentTests
 */

public class ViewHabitEventIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public ViewHabitEventIntentTests() {
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
        solo.enterText((EditText)solo.getView(R.id.filterEditText), "");
        solo.sleep(600);
    }

    public void testEdit(){

        if (((ListView)solo.getView(R.id.historyList)).getAdapter().getCount() > 0){
            solo.clickInList(1, 0);
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", HabitEventViewActivity.class);
            solo.clearEditText(0);
            solo.sleep(600);
            solo.enterText(0, "newcomment");
            solo.sleep(600);
            solo.clickOnButton("Save");
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
            solo.clickInList(1, 0);
            solo.sleep(3000);
            solo.assertCurrentActivity("wrong activity", HabitEventViewActivity.class);
            // make sure the comment was changed
            assertTrue(((EditText)solo.getView(R.id.vheCommentDynamicText)).getText().toString().equals("newcomment"));

        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}