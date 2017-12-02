/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.activities.habits.StatisticActivity;
import com.cmput301.cia.activities.templates.LocationRequestingActivity;
import com.cmput301.cia.activities.users.FollowRequestsActivity;
import com.cmput301.cia.activities.users.RankingsActivity;
import com.cmput301.cia.activities.users.SearchUsersActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.activities.users.UserProfileFragment;
import com.cmput301.cia.activities.users.ViewEventsMapActivity;
import com.cmput301.cia.activities.users.ViewFollowedUsersActivity;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeTabbedActivity extends LocationRequestingActivity {

    private static final int CREATE_EVENT = 1, CREATE_HABIT = 2, VIEW_HABIT = 3, VIEW_HABIT_HISTORY = 4, VIEW_PROFILE = 5,
            FOLLOWED_USERS = 6, SEARCH_USERS = 7, FOLLOW_REQUESTS = 8;

    public static final String ID_PROFILE = "User";

    // Profile of the signed in user
    private Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tabbed);

        loadCurrentUser();
        initializeBottomBar();
    }

    @Override
    protected void handleLocationGranted() {
        Location location = DeviceUtilities.getLocation(this);
        if (location == null)
            return;

        Intent intent = new Intent(this, ViewEventsMapActivity.class);
        intent.putExtra(ViewEventsMapActivity.ID_EVENTS, (Serializable) user.getNearbyEvents(location));
        startActivity(intent);
    }

    private void loadCurrentUser() {
        Intent intent = getIntent();
        user = (Profile) intent.getSerializableExtra(ID_PROFILE);
        if (user.hasValidId())
            user.load();

        user.synchronize();

        // handle any habits that may have been missed since the user's last login
        Date currentDate = new Date();
        if (user.getLastLogin() != null && !DateUtilities.isSameDay(user.getLastLogin(), currentDate)) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(user.getLastLogin());

            // go through each date between the user's last login and the current date
            while (!DateUtilities.isSameDay(calendar.getTime(), currentDate)){
                // update all events at the end of that date, to make sure they are marked as missed if they weren't completed
                // on that day
                user.onDayEnd(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        }

        user.setLastLogin(currentDate);
        user.save();
    }

    private void initializeBottomBar() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_dashboard:
                        onDashboardClicked();
                        break;
                    case R.id.tab_search:
                        onSearchClicked();
                        break;
                    case R.id.tab_addHabit:
                        break;
                    case R.id.tab_followRequests:
                        onFollowRequestClicked();
                        break;
                    case R.id.tab_profile:
                        onProfileClicked();
                        break;
                }
            }
        });

        bottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
            @Override
            public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
                if (newTabId == R.id.tab_addHabit) {
                    onAddHabitClicked();
                    return true;
                }
                return false;
            }
        });
    }

    private void onDashboardClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homeFragmentContainer, DashboardActivity.create(user));
        ft.commit();
    }

    private void onSearchClicked() {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.homeFragmentContainer, SearchUsersActivity.create(user));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

    }

    private void onAddHabitClicked() {
        Intent intent = new Intent(this, CreateHabitActivity.class);
        if (user.getHabitCategories() != null) {
            List<String> types = new ArrayList<>();
            types.addAll(user.getHabitCategories());
            intent.putStringArrayListExtra("types", (ArrayList<String>) types);
        }
        startActivityForResult(intent, CREATE_HABIT);
    }

    private void onFollowRequestClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homeFragmentContainer, FollowRequestsActivity.create(user));
        ft.commit();
    }

    private void onProfileClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homeFragmentContainer, UserProfileFragment.create(user,user));
        ft.commit();
    }

    //Create the menu object
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Menu item onclick bridge to specific activity.
    //use startActivityForResult instead of startActivity for return value or refresh home page.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_button_My_Profile:
//                Intent intent_My_Profile = new Intent(this, UserProfileActivity.class);
//                intent_My_Profile.putExtra(UserProfileActivity.PROFILE_ID, user);
//                intent_My_Profile.putExtra(UserProfileActivity.USER_ID, user);
//                startActivityForResult(intent_My_Profile, VIEW_PROFILE);
//                return true;
//            case R.id.menu_button_Add_New_Habit:
//                Intent intent = new Intent(this, CreateHabitActivity.class);
//                if (user.getHabitCategories() != null) {
//                    List<String> types = new ArrayList<>();
//                    types.addAll(user.getHabitCategories());
//                    intent.putStringArrayListExtra("types", (ArrayList<String>) types);
//                }
//                startActivityForResult(intent, CREATE_HABIT);
//                return true;
            case R.id.menu_button_Statistic:
                Intent intent_Statistic = new Intent(this, StatisticActivity.class);
                intent_Statistic.putExtra(StatisticActivity.ID_USER, user);
                startActivity(intent_Statistic);
                return true;
            case R.id.menu_button_Habit_History:
                Intent intent_Habit_History = new Intent(this, HistoryActivity.class);
                intent_Habit_History.putExtra(HistoryActivity.ID_PROFILE, user);
                startActivityForResult(intent_Habit_History, VIEW_HABIT_HISTORY);
                return true;
            case R.id.menu_button_My_Following:
                Intent intent_My_Following = new Intent(this, ViewFollowedUsersActivity.class);
                intent_My_Following.putExtra(ViewFollowedUsersActivity.ID_VIEWED, user);
                startActivityForResult(intent_My_Following, FOLLOWED_USERS);
                return true;
            case R.id.menu_button_PowerRankings:
                Intent intentPR = new Intent(this, RankingsActivity.class);
                intentPR.putExtra(RankingsActivity.ID_ISPOWER, true);
                startActivity(intentPR);
                return true;
            case R.id.menu_button_OverallRankings:
                Intent intentOR = new Intent(this, RankingsActivity.class);
                intentOR.putExtra(RankingsActivity.ID_ISPOWER, false);
                startActivity(intentOR);
                return true;
//            case R.id.menu_button_searchUsers:
//                Intent search = new Intent(this, SearchUsersActivity.class);
//                search.putExtra(SearchUsersActivity.ID_USER, user);
//                startActivityForResult(search, SEARCH_USERS);
//                return true;
//            case R.id.menu_button_FollowRequests:
//                Intent requests = new Intent(this, FollowRequestsActivity.class);
//                requests.putExtra(FollowRequestsActivity.ID_PROFILE, user);
//                startActivityForResult(requests, FOLLOW_REQUESTS);
//                return true;
            case R.id.menu_button_nearbyEvents:
                requestLocationPermissions();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}