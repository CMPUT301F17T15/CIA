/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.CreateHabitActivity;
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by gsp on 2017-11-12.
 */

public class CreateHabitIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public CreateHabitIntentTests(){super(com.cmput301.cia.activities.MainActivity.class);}

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "abc");
        solo.clickOnButton("Login");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        solo.clickOnButton(R.id.CreateNewHabitButton);
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);
    }
}
