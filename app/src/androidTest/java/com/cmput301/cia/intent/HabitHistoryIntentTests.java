/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

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
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        // login and navigate to habit history
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
    }

    public void testNavigation()throws Exception{
        // test current activity
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
        // test return button works?
        solo.clickOnButton("Return");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);

        // test habits button words?
        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);
    }

    public void testCheck()throws Exception{
        // test can check works?
        boolean actual1 = solo.isCheckBoxChecked("Type");
        assertEquals("Filter is Selected",false, actual1);
        // if click?
        solo.clickOnCheckBox(0);
        solo.sleep(300);
        boolean actual2 = solo.isCheckBoxChecked("Type");
        assertEquals("Filter is not Selected",true, actual2);
    }

    public void testEditText()throws Exception{
        // try to enter the text
        solo.clickOnEditText(0);
        String strInput = "Sample code 1";
        solo.sleep(300);
        solo.enterText(0, strInput);
        solo.sleep(300);
        boolean actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
        // clear all text
        solo.clearEditText(0);
        solo.sleep(300);
        strInput = "Sample code 2";
        solo.typeText(0, strInput);
        actual = solo.searchEditText(strInput);
        assertEquals("text entered is not matching",true, actual);
    }
}
