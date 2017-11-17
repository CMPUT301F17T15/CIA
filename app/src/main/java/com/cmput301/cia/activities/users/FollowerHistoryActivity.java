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
 * This activity displays the recent habit history of all the people the user is following
 */

public class FollowerHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_history);
    }
}
