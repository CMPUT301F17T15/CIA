/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.HabitEventViewActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 23 2017
 *
 * This class tests the UI for viewing habit events
 * NOTE: Delete habit event is already tested in setUp() for CreateHabitEventIntentTests
 */

public class ViewHabitEventIntentTests extends ActivityInstrumentationTestCase2<HistoryActivity> {

    private Solo solo;

    public ViewHabitEventIntentTests() {
        super(com.cmput301.cia.activities.events.HistoryActivity.class);
    }

    public void setUp() throws Exception{
        Profile profile = new Profile("XYZ");
        Habit habit = new Habit("T1", "", new Date(), new ArrayList<Integer>(), "");
        habit.setId("one");
        habit.addHabitEvent(new HabitEvent(""));
        habit.getEvents().get(0).setId("two");
        profile.addHabit(habit);

        Intent intent = new Intent();
        intent.putExtra(HistoryActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
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

    public void testCancel(){
        // change the comment to "newcomment" just for testing purposes
        testEdit();
        solo.goBack();
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);

        if (((ListView)solo.getView(R.id.historyList)).getAdapter().getCount() > 0){
            solo.clickInList(1, 0);
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", HabitEventViewActivity.class);
            solo.clearEditText(0);
            solo.sleep(600);
            solo.enterText(0, "new2");
            solo.sleep(600);
            solo.goBack();
            solo.sleep(3000);
            solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
            solo.clickInList(1, 0);
            solo.sleep(3000);
            solo.assertCurrentActivity("wrong activity", HabitEventViewActivity.class);
            // make sure the comment was not changed
            assertTrue(((EditText)solo.getView(R.id.vheCommentDynamicText)).getText().toString().equals("newcomment"));

        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}