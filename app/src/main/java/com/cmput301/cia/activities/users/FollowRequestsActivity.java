/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301.cia.R;

/**
 * @author
 * @version 1
 * Date: Nov 1X, 2017
 *
 * This activity allows the user to see all the follow requests other users have sent them
 */

public class FollowRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);
    }
}
