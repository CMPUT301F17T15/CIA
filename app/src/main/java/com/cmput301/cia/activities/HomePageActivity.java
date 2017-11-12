/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.models.AddHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.R;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.SetUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Version 5
 * Authors: Adil Malik, Shipin Guan
 * Date: Nov 11 2017
 *
 * Represents the home page the user sees after signing in
 * Keeps track of the user's information, and handles results
 * from other activities
 */

public class HomePageActivity extends AppCompatActivity {

    // Codes to keep track of other activities
    private static final int CREATE_EVENT = 1;
    private static final int CREATE_HABIT = 2, VIEW_HABIT = 3, VIEW_HABIT_HISTORY = 4;


    // Intent extra data identifier for the name of the user who signed in
    public static final String ID_USERNAME = "User", ID_NEW_ACCOUNT = "New";

    // Profile of the signed in user
    private Profile user;

    //ExpandableListView for displaying habit types as parent and habits as childs
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter adapter;
    private ListView checkable;
    private ArrayAdapter<Habit> lvc_adapter;

    // the habits the user must do today
    private List<Habit> todaysHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Create custom tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String name = intent.getStringExtra(ID_USERNAME);
        boolean newAccount = intent.getBooleanExtra(ID_NEW_ACCOUNT, true);

        Profile dummy = new Profile(name);
        // search for the user by their name
        Map<String, String> values = new HashMap<>();
        values.put("name", name);
        user = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, values);
        if (user == null){

            if (!newAccount){
                Toast.makeText(this, "Could not retrieve profile from the server.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            user = dummy;
            user.save();
        }

        // initialize the list of all habits the user has
        expandableListView = (ExpandableListView) findViewById(R.id.HabitTypeExpandableListView);
        adapter = new ExpandableListViewAdapter(HomePageActivity.this, user);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group, int child, long childRowId) {

                String category = SetUtilities.getItemAtIndex(user.getHabitCategories(), group);
                Habit habit = user.getHabitsInCategory(category).get(child);
                Toast.makeText(HomePageActivity.this, " Viewing Habit: " + adapter.getChild(group, child) + "'s detail. ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, HabitViewActivity.class);
                intent.putExtra("Habit", habit);
                intent.putExtra("HabitID", habit.getId());
                startActivityForResult(intent, VIEW_HABIT);

                return false;
            }
        });

        // the habits the user needs to do today
        todaysHabits = user.getTodaysHabits();

        // TODO: prevent the box from being unchecked
        // today's tasks listview (checkable)
        checkable = (ListView) findViewById(R.id.TodayToDoListView);
        checkable.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvc_adapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, todaysHabits);
        checkable.setAdapter(lvc_adapter);

    }

    //button on activity_home_page bridge to activity_create_habit
    public void newHabit(View view){
        if (user.getHabitCategories() != null) {
            List<String> types = new ArrayList<String>();
            types.addAll(user.getHabitCategories());
            System.out.println(types);
            Intent intent = new Intent(this, CreateHabitActivity.class);
            intent.putStringArrayListExtra("types", (ArrayList<String>) types);
            startActivityForResult(intent, CREATE_HABIT);
        }
        else{
            Intent intent = new Intent(this, CreateHabitActivity.class);
            intent.putStringArrayListExtra("types", null);
            startActivityForResult(intent, CREATE_HABIT);
        }
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
            case R.id.menu_button_My_Profile:
                Intent intent_My_Profile = new Intent(this, UserProfileActivity.class);
                intent_My_Profile.putExtra(UserProfileActivity.PROFILE_ID, user.getId());
                intent_My_Profile.putExtra(UserProfileActivity.USER_ID, user.getId());
                startActivity(intent_My_Profile);
                return true;
            case R.id.menu_button_Add_New_Habit:
                if (user.getHabitCategories() != null) {
                    List<String> types = new ArrayList<String>();
                    types.addAll(user.getHabitCategories());
                    System.out.println(types);
                    Intent intent = new Intent(this, CreateHabitActivity.class);
                    intent.putStringArrayListExtra("types", (ArrayList<String>) types);
                    startActivityForResult(intent, CREATE_HABIT);
                }
                else{
                    Intent intent = new Intent(this, CreateHabitActivity.class);
                    intent.putStringArrayListExtra("types", null);
                    startActivityForResult(intent, CREATE_HABIT);
                }
                return true;
            case R.id.menu_button_Statistic:
                Intent intent_Statistic = new Intent(this, StatisticActivity.class);
                startActivity(intent_Statistic);
                return true;
            case R.id.menu_button_Habit_History:
                Intent intent_Habit_History = new Intent(this, HistoryActivity.class);
                intent_Habit_History.putExtra("ID", user.getId());
                // TODO: pass in profile through serializable instead if it is better
                startActivityForResult(intent_Habit_History, VIEW_HABIT_HISTORY);
                return true;
            case R.id.menu_button_My_Following:
                Intent intent_My_Following = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_My_Following);
                return true;
            case R.id.menu_button_PowerRankings:
                Intent intentPR = new Intent(this, CreateHabitActivity.class);
                startActivity(intentPR);
                return true;
            case R.id.menu_button_OverallRankings:
                Intent intentOR = new Intent(this, CreateHabitActivity.class);
                startActivity(intentOR);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user.synchronize();
        adapter.refresh();
        adapter.notifyDataSetChanged();
        lvc_adapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, todaysHabits);
        checkable.setAdapter(lvc_adapter);
        checkCompletedEvents();
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
                HabitEvent event = (HabitEvent) data.getSerializableExtra(CreateHabitEventActivity.RETURNED_HABIT);
                String habitId = data.getStringExtra(CreateHabitEventActivity.ID_HABIT_HASH);
                OfflineEvent addEvent = new AddHabitEvent(habitId, event);
                user.tryHabitEvent(addEvent);
                user.save();
            }
        }
        //Read result from create habit activity
        else if(requestCode == CREATE_HABIT) {
            if(resultCode == RESULT_OK) {
                Habit habit = (Habit) data.getSerializableExtra("Habit");
                user.addHabit(habit);
                user.save();

                todaysHabits = user.getTodaysHabits();

                // TODO: are these needed
                adapter.refresh();
                adapter.notifyDataSetChanged();
                lvc_adapter.notifyDataSetChanged();

            }

        }
        else if (requestCode == VIEW_HABIT){
            if (resultCode == RESULT_OK){

                // TODO: user.removeHabitById
                String id = data.getStringExtra("HabitID");
                user.removeHabit(user.getHabitById(id));

                todaysHabits = user.getTodaysHabits();

                user.save();
                adapter.notifyDataSetChanged();
                lvc_adapter.notifyDataSetChanged();

            }
        } else if (requestCode == VIEW_HABIT_HISTORY){
            // reload to account for possibly deleted events
            // TODO: test
            user.load();
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
                checkable.performItemClick(lvc_adapter.getView(index, null, null), index, lvc_adapter.getItemId(index));
            }
        }

        // set the listener to handle event completions
        checkable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // This item is already clicked, prevent it from being disabled
                // TODO
                if (DateUtilities.isSameDay(todaysHabits.get(i).getLastCompletionDate(), new Date())){
                    return;
                }

                String checkedItems = ((TextView)view).getText().toString();
                //Toast.makeText(HomePageActivity.this, "Congratulations! you have completed " + checkedItems, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, CreateHabitEventActivity.class);
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_NAME, checkedItems);
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_HASH, todaysHabits.get(i).getId());
                startActivityForResult(intent, CREATE_EVENT);
            }
        });
    }

}


