
/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.FilterEventsActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Habit;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * This class tests the UI for logging into the system and creating
 * a new profile
 */

public class FilterEventsIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public FilterEventsIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        // login and navigate to habit history
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);

    }

    public void testFinishAndCancel() throws NoSuchFieldException, IllegalAccessException {
        assertEquals("", ((TextView)solo.getView(R.id.filterHabitText)).getText().toString());
        solo.clickInList(1, 0);

        String title = ((ListView)solo.getView(R.id.filterListView)).getAdapter().getItem(0).toString();
        assertEquals(title, ((TextView)solo.getView(R.id.filterHabitText)).getText().toString());

        solo.clickOnButton("Finish");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        // make sure that the history activity was notified of the habit selection
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("filterHabit");
        field.setAccessible(true);
        assertEquals(((Habit)field.get(solo.getCurrentActivity())).getTitle(), title);

        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);

        // make sure that the selected habit does not change if nothing was selected in the activity
        solo.clickOnButton("Finish");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        assertEquals(((Habit)field.get(solo.getCurrentActivity())).getTitle(), title);

        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterEventsActivity.class);

        // make sure the selected habit does not change if the activity was cancelled
        solo.clickOnButton("Cancel");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        assertEquals(((Habit)field.get(solo.getCurrentActivity())).getTitle(), title);

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}