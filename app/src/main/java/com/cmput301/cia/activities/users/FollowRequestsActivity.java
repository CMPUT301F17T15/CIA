/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.models.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jessica Prieto
 * @version 1
 * Date: Nov 24, 2017
 *
 * This activity allows the user to see all the follow requests other users have sent them
 */

public class FollowRequestsActivity extends AppCompatActivity {
    public static final String ID_PROFILE = "User";

    private Profile user;
    private String name;
    private List<Profile> followRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);

        Intent intent = getIntent();
        user = (Profile) intent.getSerializableExtra(SearchUsersActivity.ID_USER);

        RecyclerView rvFollowerRequests = (RecyclerView) findViewById(R.id.rvFollowerRequests);
        followRequests = user.getFollowRequests();
        FollowersRequestAdapter adapter = new FollowersRequestAdapter(this, followRequests, user);

        rvFollowerRequests.setAdapter(adapter);
        rvFollowerRequests.setLayoutManager(new LinearLayoutManager(this));
    }
}
