/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license. */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** * Version 2 * Author: Guanfang Dong
 * Date: Nov 5 2017
 *
 * This is history activity.

*  User allows to view their history habits and filter by date and comment.
 */
public class HistoryActivity extends AppCompatActivity {

    private static final int FILTER_CODE = 0;

    private List<String> habitList;
    private ArrayAdapter<String> adapter;
    private ListView historyList;
    private EditText filterEditText;
    private CheckBox useHabit;

    private Profile user;
    private Habit filterHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        user = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, getIntent().getExtras().getString("ID"));
        historyList = (ListView) findViewById(R.id.historyList);
        filterEditText = (EditText) findViewById(R.id.filterEditText);
        useHabit = (CheckBox)findViewById(R.id.historyTypeCheckbox);

        Button historyReturnButton  = (Button) findViewById(R.id.historyReturnButton);
        Button eventButton = (Button) findViewById(R.id.historyEventButton);
        Button filter = (Button) findViewById(R.id.historyFilterButton);

        // set clicker
        eventButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, FilterEventsActivity.class);
                intent.putExtra(FilterEventsActivity.ID_USER, user.getId());
                startActivityForResult(intent, FILTER_CODE);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (useHabit.isChecked() && filterHabit != null){
                    convertEventsToString(user.getHabitHistory(filterHabit));
                } else if (!filterEditText.getText().equals("")) {
                    convertEventsToString(user.getHabitHistory(filterEditText.getText().toString()));
                } else {
                    convertEventsToString(user.getHabitHistory());
                }
            }
        });

        historyReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        convertEventsToString(user.getHabitHistory());
    }

    private void convertEventsToString(List<HabitEvent> events){
        habitList = new ArrayList<>(events.size());

        Comparator<HabitEvent> c = new Comparator<HabitEvent>() {
            public int compare(HabitEvent u1, HabitEvent u2) {
                return u1.getDate().compareTo(u2.getDate());
            }
        };

        // TODO: store habitId in HabitEvent so that complexity reduced down to O(nlogm)
        for (HabitEvent event : events) {
            for (Habit habit : user.getHabits()) {
                // TODO: verify ordering is still correct
                int index = Collections.binarySearch(habit.getEvents(), event, c);
                if (index >= 0) {
                    habitList.add("Completed " + habit.getTitle() + " on " + event.getDate());
                }
            }
        }

        adapter = new ArrayAdapter<>(this, R.layout.list_item, habitList);
        historyList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                String habitId = data.getStringExtra(FilterEventsActivity.RETURNED_HABIT_ID);
                if (!habitId.equals("")) {
                    filterHabit = user.getHabitById(habitId);
                }
            }

        }
    }

}