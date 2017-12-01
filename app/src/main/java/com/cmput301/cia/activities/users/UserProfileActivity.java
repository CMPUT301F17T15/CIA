/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;

/**
 * @author Jessica Prieto
 * @version 4
 * Date: Nov 30 2017
 *
 * This activity displays the information about a user's profile
 */


public class UserProfileActivity extends AppCompatActivity {

    public static final String PROFILE_ID = "Profile", USER_ID = "User";
    public static final String RESULT_PROFILE_ID = "Profile";

    // the profile being displayed
    private Profile displayed;
    // the currently signed in user
    private Profile viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        displayed = (Profile) intent.getSerializableExtra(PROFILE_ID);
        viewer = (Profile) intent.getSerializableExtra(USER_ID);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.profileFragmentContainer, UserProfileFragment.create(displayed,viewer));
        ft.commit();

    }
}
