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
 * Version 4
 * Author: Adil Malik
 * Modified by: Shipin Guan
 * Date: Nov 9 2017
 *
 * Repressents the home page the user sees after signing in
 * Keeps track of the user's information, and handles results
 * from other activities
 */

public class HomePageActivity extends AppCompatActivity {

    // Codes to keep track of other activities
    private static final int NEW_EVENT = 1;
    private static final int CREAT_HABIT_EVENT = 2;

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
        final List<Habit> habitList = ElasticSearchUtilities.getListOf(Habit.TYPE_ID, Habit.class, values);
        habitList.add(new Habit("10km Running", "dg", new Date(), new ArrayList<Integer>(),"type1"));
        HashMap<String, List<String>> types = new HashMap<String, List<String>>();
        for(Habit h : user.getHabits()) {
            if(types.containsKey(h.getType())){
                types.get(h.getType()).add(h.getTitle());
            }else{
                types.put(h.getType(), new ArrayList<String>());
                types.get(h.getType()).add(h.getTitle());
            }
        }

        // TODO: initialize ...
        /*
        for (int i = 0; i< habitList.length(); i++)(
                //adapter.add(habitList[i].gettype());
                //adapter.addhabit()
                array.add
                )*/
        //linking expandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.HabitTypeExpandableListView);
        final ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(HomePageActivity.this, types);
        expandableListView.setAdapter(adapter);
        //activity for expandableListView
        //data hard coded need to change to serialized data
        final String[] HabitTypes = adapter.getHabitTypes();
        final String[][] Habits = adapter.getHabits();
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group, int child, long childRowId) {
                Toast.makeText(HomePageActivity.this,"Habit "+Habits[group][child] + " Clicked from types "+ HabitTypes[group],
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, HabitViewActivity.class);
                intent.putExtra("HabitName", habitList.get(0).getTitle());
                intent.putExtra("HabitType", habitList.get(0).getType());
                intent.putExtra("HabitReason", habitList.get(0).getReason());
                intent.putExtra("StartingDate", habitList.get(0).getStartDate());
                startActivityForResult(intent,2);
                return false;
            }
        });

        //Checkable listView
        //String array for testing, needed to be replaced by serialized data.
        String[] items = {"10km Running", "100 push-up", "100 sit-up", "100 squats"};
        ListView checkable = (ListView) findViewById(R.id.TodayToDoListView);
        checkable.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> lvc_adapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, items);
        // TODO: replace above line with the below one after habits are saved in profile
        //ArrayAdapter<Habit> lvc_adapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, user.getTodaysHabits());
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
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_HASH, "");//habitList.get(i).getId());
                intent.putExtra(CreateHabitEventActivity.ID_HABIT_INDEX, i);
                startActivityForResult(intent, NEW_EVENT);

            }
        });

    }
    //button on activity_home_page bridge to activity_create_habit
    public void newHabit(View view){
        Intent intent = new Intent(this, CreateHabitActivity.class);
        startActivityForResult(intent, 2);
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
                startActivityForResult(intent_new_Habit, 2);
                return true;
            case R.id.menu_button_Statistic:
                Intent intent_Statistic = new Intent(this, StatisticActivity.class);
                startActivity(intent_Statistic);
                return true;
            case R.id.menu_button_Habit_History:
                Intent intent_Habit_History = new Intent(this, HistoryActivity.class);
                intent_Habit_History.putExtra("ID", user.getId());
                startActivity(intent_Habit_History);
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

        // TODO: move some of the expandablelistview adapter stuff from onCreate() to here

        /*counterArrayAdapter = new ArrayAdapter<>(this,
                R.layout.list_item, new ArrayList<Habit>());//counters);
        countersList.setAdapter(counterArrayAdapter);*/
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

        Toast.makeText(this, "In onActivityResult", Toast.LENGTH_SHORT).show();

        // When a new habit event is created
        if (requestCode == NEW_EVENT) {
            if (resultCode == RESULT_OK) {

                HabitEvent event = (HabitEvent) data.getSerializableExtra(CreateHabitEventActivity.RETURNED_HABIT);
                String habitId = data.getStringExtra(CreateHabitEventActivity.ID_HABIT_HASH);
                OfflineEvent addEvent = new AddHabitEvent(habitId, event);
                user.tryHabitEvent(addEvent);
                user.save();
            } else if (data != null){
                int index = data.getIntExtra(CreateHabitEventActivity.ID_HABIT_INDEX, 0);

                // TODO: uncheck the box that was selected
                expandableListView.performItemClick(expandableListView.getChildAt(index), index, index);
            }

            // When the details of an existing counter are being viewed
        }/* else if (requestCode == DETAILS_CODE){
            if (resultCode == RESULT_OK){
                returnFromDetails(data);
            }
        }*/

        else if(requestCode == CREAT_HABIT_EVENT) {
            if(resultCode == RESULT_OK) {
                Habit habit = (Habit) data.getSerializableExtra("Habit");
                user.addHabit(habit);
                user.save();

                HashMap<String, List<String>> types = new HashMap<String, List<String>>();
                for(Habit h : user.getHabits()) {
                    if(types.containsKey(h.getType())){
                        types.get(h.getType()).add(h.getTitle());
                    }else{
                        types.put(h.getType(), new ArrayList<String>());
                        types.get(h.getType()).add(h.getTitle());
                    }
                }
                expandableListView = (ExpandableListView) findViewById(R.id.HabitTypeExpandableListView);
                final ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(HomePageActivity.this, types);
                expandableListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }


        //adapter.notifyDataSetChanged();
    }


