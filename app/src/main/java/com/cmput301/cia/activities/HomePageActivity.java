/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.activities.habits.CreateHabitActivity;
import com.cmput301.cia.activities.habits.HabitViewActivity;
import com.cmput301.cia.activities.habits.StatisticActivity;
import com.cmput301.cia.activities.templates.LocationRequestingActivity;
import com.cmput301.cia.activities.users.FollowRequestsActivity;
import com.cmput301.cia.activities.users.RankingsActivity;
import com.cmput301.cia.activities.users.SearchUsersActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.activities.users.ViewEventsMapActivity;
import com.cmput301.cia.activities.users.ViewFollowedUsersActivity;
import com.cmput301.cia.controller.TimedAdapterViewClickListener;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.controller.CheckableListViewAdapter;
import com.cmput301.cia.controller.ExpandableListViewAdapter;
import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.R;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.FontUtilities;
import com.cmput301.cia.utilities.SetUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.OnCompleteListener;

/**
 * @author Adil Malik, Shipin Guan
 * @version 6
 * Date: Nov 12 2017
 *
 * Represents the home page the user sees after signing in
 * Keeps track of the user's information, and handles results
 * from other activities
 */

public class HomePageActivity extends LocationRequestingActivity {

    // Codes to keep track of other activities
    private static final int CREATE_EVENT = 1, CREATE_HABIT = 2, VIEW_HABIT = 3, VIEW_HABIT_HISTORY = 4, VIEW_PROFILE = 5,
        FOLLOWED_USERS = 6, SEARCH_USERS = 7, FOLLOW_REQUESTS = 8;

    // maximum number of habits and habit events a user can have
    private static final int MAX_HABIT_HISTORY_SIZE = 2000000000;

    // Intent extra data identifier for the profile of the signed in user
    public static final String ID_PROFILE = "User";

    // Profile of the signed in user
    private Profile user;

    //ExpandableListView for displaying habit types as parent and habits as childs
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter adapter;
    private ListView checkable;
    private CheckableListViewAdapter checkableAdapter;

    // the habits the user must do today
    private List<Habit> todaysHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        user = (Profile) intent.getSerializableExtra(ID_PROFILE);

        if (user.hasValidId())
            user.load();

        user.synchronize();

        //user app tour if first time login
        if (user.getFirstTimeUse() == true){
            userAppTour();
        }

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

        // initialize the list displaying all habits the user has
        expandableListView = (ExpandableListView) findViewById(R.id.HabitTypeExpandableListView);
        adapter = new ExpandableListViewAdapter(HomePageActivity.this, user);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group, int child, long childRowId) {

                String category = SetUtilities.getItemAtIndex(user.getHabitCategories(), group);
                Habit habit = user.getHabitsInCategory(category).get(child);
                //Toast.makeText(HomePageActivity.this, " Viewing Habit: " + adapter.getChild(group, child) + "'s detail. ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, HabitViewActivity.class);
                intent.putExtra("Habit", habit);

                ArrayList<String> types = new ArrayList<>();
                types.addAll(user.getHabitCategories());
                intent.putExtra("Categories", types);

                startActivityForResult(intent, VIEW_HABIT);

                return false;
            }
        });

        // the habits the user needs to do today
        todaysHabits = user.getTodaysHabits();

        // today's tasks listview (checkable)
        checkable = (ListView) findViewById(R.id.TodayToDoListView);
        checkable.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        resetCheckableListAdapter();

        findViewById(R.id.homeAddHabitButton).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {

                if (user.getHabitsCount() >= MAX_HABIT_HISTORY_SIZE){
                    Toast.makeText(HomePageActivity.this, "You've reached the maximum number of habits (" + MAX_HABIT_HISTORY_SIZE + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(HomePageActivity.this, CreateHabitActivity.class);
                if (user.getHabitCategories() != null) {
                    List<String> types = new ArrayList<>();
                    types.addAll(user.getHabitCategories());
                    intent.putStringArrayListExtra("types", (ArrayList<String>) types);
                }
                startActivityForResult(intent, CREATE_HABIT);
            }
        });
        //Buttons on the bottom of homepage
        findViewById(R.id.homeHistoryImageView).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent_Habit_History = new Intent(HomePageActivity.this, HistoryActivity.class);
                intent_Habit_History.putExtra(HistoryActivity.ID_PROFILE, user);
                startActivityForResult(intent_Habit_History, VIEW_HABIT_HISTORY);
            }
        });

        findViewById(R.id.homeProfileImageView).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent_My_Profile = new Intent(HomePageActivity.this, UserProfileActivity.class);
                intent_My_Profile.putExtra(UserProfileActivity.PROFILE_ID, user);
                intent_My_Profile.putExtra(UserProfileActivity.USER_ID, user);
                startActivityForResult(intent_My_Profile, VIEW_PROFILE);
            }
        });

        findViewById(R.id.homeFollowedImageView).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent_My_Following = new Intent(HomePageActivity.this, ViewFollowedUsersActivity.class);
                intent_My_Following.putExtra(ViewFollowedUsersActivity.ID_VIEWED, user);
                startActivityForResult(intent_My_Following, FOLLOWED_USERS);
            }
        });

        findViewById(R.id.homeRequestsImageView).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent requests = new Intent(HomePageActivity.this, FollowRequestsActivity.class);
                requests.putExtra(FollowRequestsActivity.ID_PROFILE, user);
                startActivityForResult(requests, FOLLOW_REQUESTS);
            }
        });

        findViewById(R.id.homeSearchImageView).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent search = new Intent(HomePageActivity.this, SearchUsersActivity.class);
                search.putExtra(SearchUsersActivity.ID_USER, user);
                startActivityForResult(search, SEARCH_USERS);
            }
        });

        FontUtilities.applyFontToViews(this, (ViewGroup)findViewById(R.id.homePageLayout));
    }

    /**
     * Home page functionality showcasing
     * For first time user only
     * Disabled after complete showcasing
     */
    private void userAppTour() {
        FancyShowCaseQueue showCaseQueue = new FancyShowCaseQueue();

        final FancyShowCaseView showCaseDaily = new FancyShowCaseView.Builder(this)
                .titleStyle(0, Gravity.CENTER)
                .title("Your daily task will be showing here")
                .focusOn(findViewById(R.id.TodayToDoListView))
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .build();

        final FancyShowCaseView showCaseExpandable = new FancyShowCaseView.Builder(this)
                .titleStyle(0, Gravity.AXIS_PULL_AFTER)
                .title("Habit will be sorted by type/category here")
                .focusOn(findViewById(R.id.HabitTypeExpandableListView))
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .build();

        final FancyShowCaseView showCaseSearch = new FancyShowCaseView.Builder(this)
                .title("Want more friends?\nFind them all here")
                .focusOn(findViewById(R.id.homeSearchImageView))
                .build();

        final FancyShowCaseView showCaseFollow = new FancyShowCaseView.Builder(this)
                .title("You can find all the users that you are following here")
                .focusOn(findViewById(R.id.homeFollowedImageView))
                .build();

        final FancyShowCaseView showCaseRequest = new FancyShowCaseView.Builder(this)
                .title("The follow-requests from your fans will be showing here")
                .focusOn(findViewById(R.id.homeRequestsImageView))
                .build();

        final FancyShowCaseView showCaseProfile = new FancyShowCaseView.Builder(this)
                .title("This is your profile.")
                .focusOn(findViewById(R.id.homeProfileImageView))
                .build();

        final FancyShowCaseView showCaseHistory = new FancyShowCaseView.Builder(this)
                .title("You can find your passed habit history here")
                .focusOn(findViewById(R.id.homeHistoryImageView))
                .build();

        final FancyShowCaseView showCaseMenu = new FancyShowCaseView.Builder(this)
                .title("More navigation here ---->")
                .titleStyle(0, Gravity.TOP)
                .titleStyle(0, Gravity.LEFT)
                .build();

        final FancyShowCaseView showCaseHabit = new FancyShowCaseView.Builder(this)
                .title("Now, you can create your first habit here")
                .focusOn(findViewById(R.id.homeAddHabitButton))
                .build();

        showCaseQueue.add(showCaseExpandable);
        showCaseQueue.add(showCaseDaily);
        showCaseQueue.add(showCaseHistory);
        showCaseQueue.add(showCaseProfile);
        showCaseQueue.add(showCaseRequest);
        showCaseQueue.add(showCaseFollow);
        showCaseQueue.add(showCaseSearch);
        showCaseQueue.add(showCaseMenu);
        showCaseQueue.add(showCaseHabit);
        showCaseQueue.show();

        showCaseQueue.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                user.setFirstTimeUse(false);
                user.save();
            }
        });
    }

    /**
     * Handle the results of the request location permission being granted
     */
    @Override
    protected void handleLocationGranted() {
        Location location = DeviceUtilities.getLocation(this);
        if (location == null)
            return;

        Intent intent = new Intent(this, ViewEventsMapActivity.class);
        intent.putExtra(ViewEventsMapActivity.ID_EVENTS, (Serializable) user.getNearbyEvents(location));
        startActivity(intent);
    }

    /**
     * Create the menu object
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu item onclick bridge to specific activity.
     * Use startActivityForResult instead of startActivity for return value or refresh home page.
     * @param item
     * @return
     */
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

    /**
     * onStart refresh the main page
     */
    @Override
    protected void onStart() {
        super.onStart();
        user.synchronize();
        refreshDisplay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Handle the results of an activity that has finished
     * @param requestCode the activity's identifying code
     * @param resultCode the result status of the finished activity
     * @param data the activity's returned intent information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When a new habit event is created
        if (requestCode == CREATE_EVENT) {
            if (resultCode == RESULT_OK) {

                // successful event creation
                HabitEvent event = (HabitEvent) data.getSerializableExtra(CreateHabitEventActivity.RETURNED_HABIT);
                String habitId = data.getStringExtra(CreateHabitEventActivity.ID_HABIT_HASH);
                Habit habit = user.getHabitById(habitId);
                if (habit == null)
                    return;

                // fix double-clicking an item in the today's task list creating 2 events at once
                // TODO
                if (DateUtilities.isSameDay(habit.getLastCompletionDate(), event.getDate()))
                    return;

                OfflineEvent addEvent = new AddHabitEvent(habitId, event);
                user.tryHabitEvent(addEvent);
                user.save();

                // update the today's tasks list
                checkCompletedEvents();
            }
        }
        //Read result from create habit activity
        else if(requestCode == CREATE_HABIT) {
            if(resultCode == RESULT_OK) {
                // add a new habit
                Habit habit = (Habit) data.getSerializableExtra("Habit");

                // save the habit so that it has a valid ID
                // this is necessary for habit events, since they need to refer to the habit's ID
                if (habit.save()){
                    user.addHabit(habit);
                    user.save();
                    todaysHabits = user.getTodaysHabits();

                    adapter = new ExpandableListViewAdapter(HomePageActivity.this, user);
                    expandableListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    resetCheckableListAdapter();
                } else {
                    Toast.makeText(this, "There was an error connecting to the database. Habit was not saved.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        //result from delete habit button
        else if (requestCode == VIEW_HABIT){
            if (resultCode == RESULT_OK){

                // whether the habit was deleted or updated
                boolean deleted = data.getBooleanExtra("Deleted", false);
                if (deleted) {
                    String id = data.getStringExtra("HabitID");
                    user.removeHabit(user.getHabitById(id));
                } else {
                    // update the habit
                    Habit habit = (Habit) data.getSerializableExtra("Habit");
                    for (Habit h : user.getHabits()){
                        if (h.equals(habit)){
                            h.copyFrom(habit);
                            break;
                        }
                    }
                }

                adapter = new ExpandableListViewAdapter(HomePageActivity.this, user);
                expandableListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                todaysHabits = user.getTodaysHabits();
                resetCheckableListAdapter();
                user.save();

            }
        } else if (requestCode == VIEW_HABIT_HISTORY){
            if (resultCode == RESULT_OK) {
                user.copyFrom((Profile) data.getSerializableExtra(HistoryActivity.ID_PROFILE), true);
                user.save();

                todaysHabits = user.getTodaysHabits();
                checkableAdapter.notifyDataSetChanged();
                checkCompletedEvents();
            }
        } else if (requestCode == VIEW_PROFILE){
            if (resultCode == RESULT_OK){
                user.copyFrom((Profile) data.getSerializableExtra(UserProfileActivity.RESULT_PROFILE_ID), false);
                user.save();
            }
        } else if (requestCode == FOLLOWED_USERS){
            if (resultCode == RESULT_OK){
                List<Profile> followed = (List<Profile>) data.getSerializableExtra(ViewFollowedUsersActivity.RETURNED_FOLLOWED);
                for (Profile toFollow : followed) {
                    user.follow(toFollow);
                }
            }
        } else if (requestCode == SEARCH_USERS){
            if (resultCode == RESULT_OK){
                user.copyFrom((Profile) data.getSerializableExtra(SearchUsersActivity.RETURNED_PROFILE), false);
                user.save();
            }
        } else if (requestCode == FOLLOW_REQUESTS){
            if (resultCode == RESULT_OK){
                // TODO
                /*Profile result = (Profile) data.getSerializableExtra(SearchUsersActivity.RETURNED_PROFILE);
                user.copyFrom(result);
                user.save();*/
            }
        }

    }

    /**
     * Automatically check all habits that the user has completed today
     */
    private void checkCompletedEvents(){
        // reset the listener so that a new event is not created
        checkable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        // automatically check the events that have already been completed today
        for (int index = 0; index < todaysHabits.size(); ++index) {
            if (DateUtilities.isSameDay(todaysHabits.get(index).getLastCompletionDate(), new Date())) {
                checkable.performItemClick(checkableAdapter.getView(index, null, null), index, checkableAdapter.getItemId(index));
            }
        }

        // set the listener to handle event completions
        checkable.setOnItemClickListener(new /*AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {*/
         TimedAdapterViewClickListener() {
             @Override
             public void handleClick(View view, int i) {

                if (user.getHabitHistory().size() >= MAX_HABIT_HISTORY_SIZE){
                    // refresh the list to undo the item being selected
                    resetCheckableListAdapter();
                    checkCompletedEvents();
                    Toast.makeText(HomePageActivity.this, "You've reached the maximum number of habit events (" + MAX_HABIT_HISTORY_SIZE + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                // start the activity to create a habit event for this completion
                String checkedItems = ((TextView)view).getText().toString();
                Intent intent = new Intent(HomePageActivity.this, CreateHabitEventActivity.class);
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_NAME, checkedItems);
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_HASH, todaysHabits.get(i).getId());
                startActivityForResult(intent, CREATE_EVENT);
            }
        });
    }

    /**
     * Update the checkable list view adapter for the "today's tasks" list
     */
    private void resetCheckableListAdapter(){
        checkableAdapter = new CheckableListViewAdapter(this, R.layout.checkable_list_view, R.id.CheckedTextView, todaysHabits);
        checkable.setAdapter(checkableAdapter);
    }

    @Override
    protected void onResume() {
        refreshDisplay();
        super.onResume();
    }

    /**
     * Update the elements in both ListViews
     */
    private void refreshDisplay(){
        // update the habits list display
        adapter.refresh();
        adapter.notifyDataSetChanged();

        // update the today's task list
        resetCheckableListAdapter();
        checkCompletedEvents();
    }
}


