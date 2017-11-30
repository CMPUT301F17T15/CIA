/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ViewSwitcher;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.templates.LocationRequestingActivity;
import com.cmput301.cia.controller.ButtonClickListener;
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Follow;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.models.Triple;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 13 2017
 *
 * This activity displays all of the people a viewer is following
 */

public class ViewFollowedUsersActivity extends LocationRequestingActivity {

    // Intent identifiers for passed in profiles
    public static final String ID_VIEWED = "Profile", ID_USER = "User";

    // Return identifiers for the activity result
    public static final String RETURNED_FOLLOWED = "Followed", RETURNED_ISUSER = "User";

    // Activity result codes
    private static final int VIEW_PROFILE = 0;

    // the displayed being displayed
    private Profile displayed;
    // the currently signed in viewer
    private Profile viewer;

    // list of followed users
    private ListView followedList;
    private ArrayAdapter<Profile> followedListAdapter;
    private List<Profile> followed;
    private List<String> followingIds;

    // list of followed viewer's habits
    private ListView habitsList;
    private ArrayAdapter<Habit> habitsListAdapter;

    // list of followed viewer's events
    private ListView eventsList;
    private ArrayAdapter<CompletedEventDisplay> eventsListAdapter;

    private ImageView historyImage;
    private ImageView profileImage;
    private ViewSwitcher profileHistorySwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_followed_users);

        Intent intent = getIntent();
        displayed = (Profile) intent.getSerializableExtra(ID_VIEWED);
        viewer = (Profile) intent.getSerializableExtra(ID_USER);

        followedList = (ListView)findViewById(R.id.vfuProfilesList);

        // TODO: encapsulation using displayed.getFollowing()
        followingIds = Follow.getFollowing(displayed.getId());
        followed = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, followingIds);
        followedListAdapter = new ArrayAdapter<>(this, R.layout.list_item, followed);
        followedList.setAdapter(followedListAdapter);

        // view a displayed when it is clicked
        followedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent profileIntent = new Intent(ViewFollowedUsersActivity.this, UserProfileActivity.class);
                profileIntent.putExtra(UserProfileActivity.PROFILE_ID, followed.get(position));
                profileIntent.putExtra(UserProfileActivity.USER_ID, viewer);
                startActivityForResult(profileIntent, VIEW_PROFILE);
            }
        });

        habitsList = (ListView)findViewById(R.id.vfuHabitsList);
        habitsListAdapter = new ArrayAdapter<>(this, R.layout.list_item, displayed.getFollowedHabits());
        habitsList.setAdapter(habitsListAdapter);
        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: display statistics
            }
        });

        eventsList = (ListView)findViewById(R.id.vfuEventsList);
        eventsListAdapter = new ArrayAdapter<>(this, R.layout.list_item, displayed.getFollowedHabitHistory());
        eventsList.setAdapter(eventsListAdapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: view event details
            }
        });

        historyImage = (ImageView)findViewById(R.id.vfuHistoryIcon);
        // switch to history mode
        historyImage.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                showHistory();
            }
        });

        profileImage = (ImageView)findViewById(R.id.vfuUsersIcon);
        // switch to user mode
        profileImage.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                showProfiles();
            }
        });

        // switch to map activity when the map image is clicked
        findViewById(R.id.vfuMapView).setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                requestLocationPermissions();
            }
        });

        // toggle the history/habit view when the switch gets clicked
        findViewById(R.id.vfuHistoryEventsSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchHistoryView();
            }
        });

        profileHistorySwitcher = (ViewSwitcher)findViewById(R.id.vfuProfileHistorySwitcher);
        // do this because calling showProfiles() for the first time results in switching to history
        profileHistorySwitcher.showNext();
        showProfiles();
    }

    /**
     * Handle the results of the request location permission being granted
     */
    @Override
    protected void handleLocationGranted() {
        Intent intent = new Intent(this, ViewEventsMapActivity.class);
        intent.putExtra(ViewEventsMapActivity.ID_EVENTS, (Serializable) displayed.getFollowedHabitHistory());
        startActivity(intent);
    }

    /**
     * Handle the back button being pressed
     */
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        // TODO: return displayed for simplicity
        returnIntent.putExtra(RETURNED_FOLLOWED, (Serializable)followingIds);
        returnIntent.putExtra(RETURNED_ISUSER, displayed.equals(viewer));
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        followingIds = Follow.getFollowing(displayed.getId());
        followed = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, followingIds);
        followedListAdapter.clear();
        followedListAdapter.addAll(followed);
        followedListAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        followed = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, followingIds);
//        followedListAdapter = new ArrayAdapter<>(this, R.layout.list_item, followed);
//        followedList.setAdapter(followedListAdapter);
//    }

    /**
     * Handle the results of an activity that has finished
     * @param requestCode the activity's identifying code
     * @param resultCode the result status of the finished activity
     * @param data the activity's returned intent information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When a new habit event is created
        if (requestCode == VIEW_PROFILE) {
            if (resultCode == RESULT_OK) {
                Profile newProfile = (Profile) data.getSerializableExtra(UserProfileActivity.RESULT_PROFILE_ID);
                displayed.copyFrom(newProfile, false);
            }
        }
    }

    /**
     * Switch the view to the followed user's habits list and history
     */
    private void showHistory(){
        historyImage.setVisibility(View.INVISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileHistorySwitcher.showNext();
    }

    /**
     * Switch the view to the followed user's profiles
     */
    private void showProfiles(){
        historyImage.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.INVISIBLE);
        profileHistorySwitcher.showNext();
    }

    /**
     * Cycle between the history and event view lists
     */
    private void switchHistoryView(){
        if (habitsList.getVisibility() == View.VISIBLE){
            // switch to events view
            habitsList.setVisibility(View.INVISIBLE);
            eventsList.setVisibility(View.VISIBLE);
        } else {
            // switch to history view
            habitsList.setVisibility(View.VISIBLE);
            eventsList.setVisibility(View.INVISIBLE);
        }
    }
}
