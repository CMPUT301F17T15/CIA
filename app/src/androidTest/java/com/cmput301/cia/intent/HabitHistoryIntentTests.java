/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.util.Log;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.FilterEventsActivity;
import com.cmput301.cia.activities.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.robotium.solo.Solo;

/**
 * Created by guanfang on 2017/11/12.
 */

public class HabitHistoryIntentTests extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public HabitHistoryIntentTests(){
        super(com.cmput301.cia.activities.HistoryActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testNavigation()throws Exception{
        // test current activity
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
        // test return button works?
        solo.clickOnButton("Return");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        // test habits button words?
        solo.clickOnButton("Habits");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);
    }

    public void testCheck()throws Exception{
        // test can check works?
        boolean actual1 = solo.isCheckBoxChecked("Type");
        assertEquals("Filter is Selected",false, actual1);
        // if click?
        solo.clickOnCheckBox(5);
        solo.sleep(300);
        boolean actual2 = solo.isCheckBoxChecked("Type");
        assertEquals("Filter is not Selected",true, actual2);
    }

    public void testEditText()throws Exception{
        // try to enter the text
        solo.clickOnEditText(1);
        String strInput = "Sample code 1";
        solo.sleep(300);
        solo.enterText(1, strInput);
        solo.sleep(300);
        boolean actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
        // clear all text
        solo.clearEditText(1);
        solo.sleep(300);
        strInput = "Sample code 2";
        solo.typeText(1, strInput);
        actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
    }
}
