/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;

public class ViewFollowedUsersActivity extends AppCompatActivity {

    // Intent codes for passed in profiles
    public static final String ID_VIEWED = "Profile", ID_USER = "User";

    // Return codes for the activity result
    public static final String RETURNED_VIEWED = "Profile", RETURNED_ISUSER = "User";

    // the profile being displayed
    private Profile profile;
    // the currently signed in user
    private Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_followed_users);

        Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra(ID_VIEWED);
        user = (Profile) intent.getSerializableExtra(ID_USER);

    }
}
