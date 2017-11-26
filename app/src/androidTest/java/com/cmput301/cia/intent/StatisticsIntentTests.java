/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.habits.StatisticActivity;
import com.cmput301.cia.activities.habits.StatisticViewActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for viewing statistics about habits
 * NOTE: These tests require an internet connection
 */

public class StatisticsIntentTests extends ActivityInstrumentationTestCase2<StatisticActivity> {
    private Solo solo;

    public StatisticsIntentTests(){super(StatisticActivity.class);}

    public void setUp() throws Exception {

        List<Integer> allDays = new ArrayList<>();
        for (int i = 1; i <= 7; ++i){
            allDays.add(i);
        }

        Profile profile = new TestProfile("xyz");
        Habit habit = new Habit("T1", "", new Date(), allDays, "x");
        habit.setId("one");
        profile.addHabit(habit);

        Habit habit2 = new Habit("T55", "", new Date(), allDays, "y");
        habit2.setId("h2");
        profile.addHabit(habit2);

        Intent intent = new Intent();
        intent.putExtra(StatisticActivity.ID_USER, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
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
