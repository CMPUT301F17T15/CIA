/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomeTabbedActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.activities.users.UserProfileFragment;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests the UI for the features on the home page of the activity
 */

public class HomePageIntentTests extends ActivityInstrumentationTestCase2<HomeTabbedActivity> {

    private Solo solo;

    public HomePageIntentTests() {
        super(com.cmput301.cia.activities.HomeTabbedActivity.class);
    }

    public void setUp() throws Exception{

        Profile profile = new TestProfile("xyz");
        profile.setFirstTimeUse(false);
        Intent intent = new Intent();
        intent.putExtra(HomeTabbedActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testNavigation(){
        // select profile option in menu
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_profile));
        solo.sleep(1000);
        assertTrue(getActivity().getFragmentForCurrentTab() instanceof UserProfileFragment);

        // select add new habit
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_addHabit));
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", CreateHabitActivity.class);
        solo.goBackToActivity("HomeTabbedActivity");
        solo.sleep(300);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        // select habit history
        solo.clickOnActionBarItem(R.id.menu_button_Habit_History);
        solo.clickOnMenuItem("Habit History");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HistoryActivity.class);
        solo.goBack();
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        // TODO: power rankings

        // TODO: overall rankings

        // TODO: followed users

        // TODO: whatever else is in the menu bar

    }


    public void testTodaysHabits() throws NoSuchFieldException, IllegalAccessException {
        Field field = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field.setAccessible(true);
        Profile user = (Profile) field.get(solo.getCurrentActivity());

        ListView todaysHabits = (ListView)solo.getView(R.id.TodayToDoListView);
        assertTrue(user.getTodaysHabits().size() == todaysHabits.getAdapter().getCount());
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}