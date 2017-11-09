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
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Version 3
 * Author: Adil Malik
 * Modified by: Shipin Guan
 * Date: Nov 7 2017
 *
 * Repressents the home page the user sees after signing in
 * Keeps track of the user's information, and handles results
 * from other activities
 */

public class HomePageActivity extends AppCompatActivity {

    // Codes to keep track of other activities
    private static final int NEW_EVENT = 1;

    // Intent extra data identifier for the name of the user who signed in
    public static final String ID_USERNAME = "User";

    // Profile of the signed in user
    private Profile user;

    //ExpandableListView for displaying habit types as parent and habits as childs
    ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Create custom tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ID_USERNAME);
        Profile dummy = new Profile(name);
        Map<String, String> values = new HashMap<>();
        values.put("name", name);
        user = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, values);
        if (user == null){
            user = dummy;
            user.save();
        }

        values.clear();
        values.put("creator", user.getId());
        List<Habit> habitList = ElasticSearchUtilities.getListOf(Habit.TYPE_ID, Habit.class, values);
        /*habitList.add(new Habit("10km Running", "dg", new Date(), new ArrayList<Integer>()));
        habitList.add(new Habit("100 push-up", "dg", new Date(), new ArrayList<Integer>()));
        habitList.add(new Habit("100 sit-up", "dg", new Date(), new ArrayList<Integer>()));*/
        // TODO: initialize ...

        //linking expandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.HabitTypeExpandableListView);
        final ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(HomePageActivity.this);
        expandableListView.setAdapter(adapter);
        //activity for expandablelistview
        //data hard coded need to change to serialized data
        final String[] HabitTypes = adapter.getHabitTypes();
        final String[][] Habits = adapter.getHabits();
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int i, int i1, long l) {
                Toast.makeText(HomePageActivity.this,"Habit "+Habits[i][i1] + " Clicked from types "+ HabitTypes[i],
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, HabitViewActivity.class);
                intent.putExtra("HabitName", Habits[i][i1]);
                intent.putExtra("HabitType", HabitTypes[i]);
                startActivity(intent);
                return false;
            }
        });

        //Checkable listView
        //String array for testing, needed to be replaced by serialized data.
        String[] items = {"10km Running", "100 push-up", "100 sit-up", "100 squats"};
        ListView checkable = (ListView) findViewById(R.id.TodayToDoListView);
        checkable.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> lvc_adapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, items);
        checkable.setAdapter(lvc_adapter);

        //Onclick display toast, show congratulation on complete.

        // TODO: prevent the box from being unchecked, and make it automatically checked if getLastCompletionDate() is today's date
        checkable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String checkedItems = ((TextView)view).getText().toString();
                Toast.makeText(HomePageActivity.this, "Congratulations! you have completed " + checkedItems, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomePageActivity.this, CreateHabitEventActivity.class);
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_NAME, checkedItems);
                // TODO: a way of getting the habit's unique ID (from the user, probably using habit's title)
                //user.getHabitIdByTitle(checkedItems)
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_HASH, "");//habitList.get(i).getId());
                startActivityForResult(intent, NEW_EVENT);

            }
        });

    }
    //button on activity_home_page bridge to activity_create_habit
    public void newHabit(View view){
        Intent intent = new Intent(this, CreateHabitActivity.class);
        startActivity(intent);
    }
    //Crate the menu object
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
                Intent intent_My_Profile = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_My_Profile);
                return true;
            case R.id.menu_button_Add_New_Habit:
                Intent intent_new_Habit = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_new_Habit);
                return true;
            case R.id.menu_button_Statistic:
                Intent intent_Statistic = new Intent(this, StatisticActivity.class);
                startActivity(intent_Statistic);
                return true;
            case R.id.menu_button_Habit_History:
                Intent intent_Habit_History = new Intent(this, HistoryActivity.class);
                startActivity(intent_Habit_History);
                return true;
            case R.id.menu_button_My_Following:
                Intent intent_My_Following = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_My_Following);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*counterArrayAdapter = new ArrayAdapter<>(this,
                R.layout.list_item, new ArrayList<Habit>());//counters);
        countersList.setAdapter(counterArrayAdapter);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When a new habit event is created
        if (requestCode == NEW_EVENT) {
            if (resultCode == RESULT_OK) {

                HabitEvent event = (HabitEvent) data.getSerializableExtra(CreateHabitEventActivity.RETURNED_HABIT);
                String habitId = data.getStringExtra(CreateHabitEventActivity.ID_HABIT_HASH);
                OfflineEvent addEvent = new AddHabitEvent(habitId, event);
                user.tryHabitEvent(addEvent);
                user.save();
            }

            // When the details of an existing counter are being viewed
        }/* else if (requestCode == DETAILS_CODE){
            if (resultCode == RESULT_OK){
                returnFromDetails(data);
            }
        }*/
    }

    /**
     * Call this after successfully returning from viewing details about a counter
     * @param data is the activity's results
     */
    private void returnFromDetails(Intent data){
        // whether the counter was deleted or not
        /*boolean deleted = data.getBooleanExtra(CounterDetailsActivity.ID_DELETED, false);

        String name = data.getStringExtra(CounterDetailsActivity.ID_COUNTER_NAME);
        int index = data.getIntExtra(CounterDetailsActivity.ID_INDEX, 0);

        // The counter that was being viewed
        Counter counter = counters.get(index);

        if (deleted){
            counters.remove(counter);
            countersAmountText.setText(String.valueOf(counters.size()));
        } else {
            long value = data.getLongExtra(CounterDetailsActivity.ID_COUNTER_VALUE, 0);
            String desc = data.getStringExtra(CounterDetailsActivity.ID_COUNTER_DESC);
            long currentValue = data.getLongExtra(CounterDetailsActivity.ID_COUNTER_CVALUE, 0);

            if (counter.getInitialValue() != value)
                counter.setInitialValue(value);
            if (counter.getCurrentValue() != currentValue)
                counter.setCurrentValue(currentValue);
            counter.setName(name);
            counter.setComment(desc);
        }
        counterArrayAdapter.notifyDataSetChanged();
        saveCounters();*/
    }
}
