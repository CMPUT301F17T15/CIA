/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.DeleteHabitEvent;
import com.cmput301.cia.models.EditHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * This activity allows the user to view all of their completed habit events
 */

public class HistoryActivity extends AppCompatActivity {

    private static final int FILTER_CODE = 0, EVENT_CODE = 1;

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
        filterHabit = null;

        Button historyReturnButton  = (Button) findViewById(R.id.historyReturnButton);
        Button eventButton = (Button) findViewById(R.id.historyEventButton);
        Button filter = (Button) findViewById(R.id.historyFilterButton);

        eventButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, FilterEventsActivity.class);
                intent.putExtra(FilterEventsActivity.ID_USER, user.getId());
                startActivityForResult(intent, FILTER_CODE);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (useHabit.isChecked() && filterHabit == null){
                    Toast.makeText(HistoryActivity.this, "No filter habit was selected. Checkbox is ignored.", Toast.LENGTH_SHORT).show();
                }
                convertEventsToString();
            }
        });

        historyReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, HabitEventViewActivity.class);
                intent.putExtra("HabitEvent", getDisplayedEvents().get(position));
                startActivityForResult(intent, EVENT_CODE);
            }
        });

        convertEventsToString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        convertEventsToString();
    }

    /**
     * @return list of all habit events that are currently displayed on the screen
     */
    private List<HabitEvent> getDisplayedEvents(){
        if (useHabit.isChecked() && filterHabit != null){
            return user.getHabitHistory(filterHabit);
        } else if (!filterEditText.getText().equals("")) {
            return user.getHabitHistory(filterEditText.getText().toString());
        } else {
            return user.getHabitHistory();
        }
    }

    /**
     * Convert all displayed habit events into a string representation
     */
    private void convertEventsToString(){
        List<HabitEvent> events = getDisplayedEvents();
        habitList = new ArrayList<>(events.size());

        // binary search comparator based on event date
        /*Comparator<HabitEvent> c = new Comparator<HabitEvent>() {
            public int compare(HabitEvent u1, HabitEvent u2) {
                return u1.getDate().compareTo(u2.getDate());
            }
        };*/

        for (HabitEvent event : events) {
            Habit habit = user.getHabitById(event.getHabitId());
            habitList.add("Completed " + habit.getTitle() + " on " + event.getDate());

            /*for (Habit habit : user.getHabits()) {
                // get whether this habit contains the current event
                int index = Collections.binarySearch(habit.getEvents(), event, c);
                if (index >= 0) {
                    habitList.add("Completed " + habit.getTitle() + " on " + event.getDate());
                }
            }*/
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

        } else if (requestCode == EVENT_CODE) {
            if (resultCode == RESULT_OK) {
                boolean isDeleted = data.getBooleanExtra(HabitEventViewActivity.RETURNED_DELETED, false);
                HabitEvent event = (HabitEvent) data.getSerializableExtra(HabitEventViewActivity.RETURNED_EVENT);
                OfflineEvent offlineEvent;
                if (!isDeleted) {
                    offlineEvent = new EditHabitEvent(event);
                } else {
                    offlineEvent = new DeleteHabitEvent(event);
                }
                user.tryHabitEvent(offlineEvent);
            }

        }
    }

}