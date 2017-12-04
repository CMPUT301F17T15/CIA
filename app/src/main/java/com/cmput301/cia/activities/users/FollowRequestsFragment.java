/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.FollowersRequestAdapter;
import com.cmput301.cia.models.Profile;

import java.util.List;

/**
 * @author Jessica Prieto
 * @version 1
 * Date: Nov 24, 2017
 *
 * This activity allows the user to see all the sendFollowRequest requests other users have sent them
 */

public class FollowRequestsFragment extends Fragment {

    public static final String ID_PROFILE = "User";

    private Profile user;
    private List<Profile> followRequests;
    private FollowersRequestAdapter adapter;

    private TextView noRequests;

    public static FollowRequestsFragment create(Profile currentUser) {
        FollowRequestsFragment followRequestsFragment = new FollowRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, currentUser);
        followRequestsFragment.setArguments(args);
        return followRequestsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_follow_requests, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().setTitle("Follow Requests");
        user = (Profile) getArguments().getSerializable(SearchUsersFragment.ID_USER);
        noRequests = (TextView) view.findViewById(R.id.noFollowRequests);
        RecyclerView rvFollowerRequests = (RecyclerView) view.findViewById(R.id.rvFollowerRequests);

        followRequests = user.getFollowRequests();

        if (followRequests.size() > 0) {
            noRequests.setVisibility(View.GONE);
        }
        adapter = new FollowersRequestAdapter(getContext(), followRequests, user);

        adapter.setOnItemClickListener(new FollowersRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.PROFILE_ID, adapter.getProfile(position));
                intent.putExtra(UserProfileActivity.USER_ID, user);
                startActivity(intent);
            }
        });

        rvFollowerRequests.setAdapter(adapter);
        rvFollowerRequests.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onResume() {
        super.onResume();
        followRequests = user.getFollowRequests();
        adapter.setFollowRequests(followRequests);
    }
}
