/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.robotium.solo.Solo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 11 2017
 *
 * This class tests the UI for logging into the system and creating
 * a new profile
 */

public class LoginIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public LoginIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        Map<String, String> values = new HashMap<>();
        values.put("name", "testsignin");
        ElasticSearchUtilities.delete(Profile.TYPE_ID, Profile.class, values);
        solo.sleep(3000);
        assertTrue(ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, values) == null);
    }

    public void testCreateProfile(){
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Login");            // can not login with empty name
        solo.sleep(600);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Create Profile");         // can not register with empty name
        solo.sleep(600);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "testsignin");
        solo.sleep(1000);
        solo.clickOnButton("Login");            // can not login since profile should not exist
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clickOnButton("Create Profile");         // register new profile
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        // make sure previously created profiles can not be recreated
        solo.goBackToActivity("MainActivity");
        solo.sleep(2500);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        solo.clearEditText(0);
        solo.sleep(600);
        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "testsignin");
        solo.sleep(1000);
        solo.clickOnButton("Create Profile");
        solo.sleep(600);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        // login again
        solo.clickOnButton("Login");
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}