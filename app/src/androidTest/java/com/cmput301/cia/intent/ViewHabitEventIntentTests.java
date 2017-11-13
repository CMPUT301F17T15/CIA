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
import java.util.ArrayList;

/**
 * This class tests the UI for viewing habit events
 * NOTE: These tests require an internet connection
 */

public class ViewHabitEventIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public ViewHabitEventIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}