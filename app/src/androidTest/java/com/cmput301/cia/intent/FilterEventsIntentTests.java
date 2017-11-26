
/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.events.FilterHabitsActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * This class tests the UI for logging into the system and creating
 * a new profile
 */

public class FilterEventsIntentTests extends ActivityInstrumentationTestCase2<HistoryActivity> {

    private Solo solo;

    public FilterEventsIntentTests() {
        super(HistoryActivity.class);
    }

    public void setUp() throws Exception {

        List<Integer> allDays = new ArrayList<>();
        for (int i = 1; i <= 7; ++i){
            allDays.add(i);
        }

        Profile profile = new TestProfile("xyz");
        Habit habit = new Habit("T1", "", new Date(), allDays, "");
        habit.setId("one");
        profile.addHabit(habit);

        Habit habit2 = new Habit("T55", "", new Date(), allDays, "");
        habit2.setId("h2");
        habit2.addHabitEvent(new HabitEvent("one"));
        profile.addHabit(habit2);

        Intent intent = new Intent();
        intent.putExtra(HistoryActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.sleep(1000);
        solo.clickOnButton("Habits");
        solo.sleep(2000);
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
        solo.assertCurrentActivity("wrong activity", FilterHabitsActivity.class);

        // make sure that the selected habit does not change if nothing was selected in the activity
        solo.clickOnButton("Finish");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        assertEquals(((Habit)field.get(solo.getCurrentActivity())).getTitle(), title);

        solo.clickOnButton("Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", FilterHabitsActivity.class);

        // make sure the selected habit does not change if the activity was cancelled
        solo.goBack();
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        assertEquals(((Habit)field.get(solo.getCurrentActivity())).getTitle(), title);

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}