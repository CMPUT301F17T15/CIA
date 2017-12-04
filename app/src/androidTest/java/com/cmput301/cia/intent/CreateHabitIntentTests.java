/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomeTabbedActivity;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for creating habits
 * NOTE: These tests require an internet connection
 */

public class CreateHabitIntentTests extends ActivityInstrumentationTestCase2<HomeTabbedActivity> {
    private Solo solo;

    public CreateHabitIntentTests(){super(HomeTabbedActivity.class);}

    public void setUp() throws Exception {

        Profile profile = new TestProfile("xyz");
        profile.setFirstTimeUse(false);
        Intent intent = new Intent();

        intent.putExtra(HomeTabbedActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testClear(){
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_addHabit));
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        // no types exist previously, so a dialog requesting input pops up
        solo.enterText(solo.getEditText("Enter new type here"), "Type");
        solo.sleep(600);
        solo.clickOnView(solo.getView(R.id.Ok_Button));
        solo.sleep(1000);

        solo.enterText((EditText)solo.getView(R.id.habitReason), "reason");
        solo.sleep(500);
        solo.enterText((EditText)solo.getView(R.id.habitName), "none");
        solo.sleep(500);

        solo.clickOnButton("Clear");
        solo.sleep(1000);
        assertTrue(solo.getEditText(1).getText().toString().equals(""));
        assertTrue(solo.getEditText(0).getText().toString().equals(""));
    }

    public void testAdd() throws NoSuchFieldException, IllegalAccessException {

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        int count = ((Profile)field.get(solo.getCurrentActivity())).getHabitsCount();


        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_addHabit));
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);


        // no types exist previously, so a dialog requesting input pops up
        solo.clickOnView(solo.getView(R.id.Ok_Button));
        solo.sleep(1000);
        // dialog should not close because the new type was empty
        assertNotNull("Dialog was closed", solo.getView(R.id.Ok_Button));
        solo.enterText(solo.getEditText("Enter new type here"), "Type");
        solo.sleep(600);
        solo.clickOnView(solo.getView(R.id.Ok_Button));
        solo.sleep(1000);

        solo.enterText((EditText)solo.getView(R.id.habitReason), "reason");
        solo.sleep(500);

        solo.clickOnButton("Save");
        solo.sleep(1000);

        // should not change because no days were selected
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        // select wednesday
        solo.clickOnView(solo.getView(R.id.day_picker));
        solo.sleep(600);

        solo.clickOnButton("Save");
        solo.sleep(1000);
        // should not change because no title
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        solo.enterText((EditText)solo.getView(R.id.habitName), "nametest");
        solo.sleep(800);

        solo.clickOnButton("Save");
        solo.sleep(4000);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        // make sure a new habit was added to the user
        assertTrue(((Profile)field.get(solo.getCurrentActivity())).getHabitsCount() == count+1);
    }

    public void testLength(){
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_addHabit));
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);

        solo.enterText(solo.getEditText("Enter new type here"), "Type");
        solo.sleep(600);
        solo.clickOnView(solo.getView(R.id.Ok_Button));
        solo.sleep(1000);

        // max reason length = 30
        solo.enterText((EditText)solo.getView(R.id.habitReason), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        solo.sleep(500);
        assertTrue(((EditText)solo.getView(R.id.habitReason)).getText().toString().length() == 30);

        // max name length = 20
        solo.enterText((EditText)solo.getView(R.id.habitName), "ffffffffffffffffffffffffffffffffffffffffffffffffff");
        solo.sleep(500);
        assertTrue(((EditText)solo.getView(R.id.habitName)).getText().toString().length() == 20);

    }

}
