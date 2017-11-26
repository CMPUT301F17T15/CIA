/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.FollowersRequestAdapter;
import com.cmput301.cia.models.Profile;

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
    public static final String PROFILE_ID = "FollowerRequest";

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
        final FollowersRequestAdapter adapter = new FollowersRequestAdapter(this, followRequests, user);

        adapter.setOnItemClickListener(new FollowersRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(FollowRequestsActivity.this, UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.PROFILE_ID, adapter.getProfile(position));
                intent.putExtra(UserProfileActivity.USER_ID, user);
                startActivity(intent);
            }
        });

        rvFollowerRequests.setAdapter(adapter);
        rvFollowerRequests.setLayoutManager(new LinearLayoutManager(this));
    }
}
