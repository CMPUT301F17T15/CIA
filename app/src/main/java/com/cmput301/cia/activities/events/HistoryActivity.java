/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.templates.LocationRequestingActivity;
import com.cmput301.cia.activities.users.ViewEventsMapActivity;
import com.cmput301.cia.controller.HistoryAdapter;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.DeleteHabitEvent;
import com.cmput301.cia.models.EditHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.FontUtilities;

import java.io.Serializable;
import java.util.List;

/**
 * @author Adil Malik
 * @version 3
 * Date: Nov 23 2017
 *
 * This activity allows the user to view all of their completed habit events in a list.
 * The events can be filtered by event comment, or by their habit
 */

public class HistoryActivity extends LocationRequestingActivity {

    // Intent data identifier for the passed in and returned profile
    public static final String ID_PROFILE = "Profile";

    private static final int FILTER_CODE = 0, EVENT_CODE = 1;

    // the list of habits that are currently displayed on the screen
    private ListView historyList;

    private EditText filterEditText;
    private CheckBox useHabit;
    private TextView filterHabitText;

    // The user who is viewing their habit history
    private Profile user;

    // The habit that is selected as a filter
    private Habit filterHabit;

    // Text used for filtering by event comment
    private String filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        user = (Profile) getIntent().getSerializableExtra(ID_PROFILE);
        filterHabit = null;
        filterText = "";

        findViewById(R.id.historyLayout).requestFocus();

        historyList = (ListView) findViewById(R.id.historyList);
        filterEditText = (EditText) findViewById(R.id.filterEditText);
        useHabit = (CheckBox)findViewById(R.id.historyTypeCheckbox);
        filterHabitText = (TextView)findViewById(R.id.historyFilterHabitText);

        Button eventButton = (Button) findViewById(R.id.historyEventButton);
        Button filter = (Button) findViewById(R.id.historyFilterButton);

        // allow the user to pick a filter habit
        eventButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent = new Intent(HistoryActivity.this, FilterHabitsActivity.class);
                intent.putExtra(FilterHabitsActivity.ID_HABITS, (Serializable) user.getHabits());
                startActivityForResult(intent, FILTER_CODE);
            }
        });

        // apply filter
        filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (useHabit.isChecked() && filterHabit == null){
                    Toast.makeText(HistoryActivity.this, "No filter habit was selected. Checkbox is ignored.", Toast.LENGTH_SHORT).show();
                }
                filterText = filterEditText.getText().toString();
                convertEventsToString();
            }
        });

        // view the details of a habit event
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, HabitEventViewActivity.class);
                intent.putExtra(HabitEventViewActivity.ID_HABIT_EVENT, getDisplayedEvents().get(position).getEvent());
                intent.putExtra(HabitEventViewActivity.ID_HABIT_NAME, getDisplayedEvents().get(position).getHabitName());
                startActivityForResult(intent, EVENT_CODE);
            }
        });

        convertEventsToString();
//        FontUtilities.applyFontToViews(this, (ViewGroup)findViewById(R.id.historyLayout));
    }

    @Override
    protected void onStart() {
        super.onStart();
        convertEventsToString();
    }

    /**
     * @return list of all habit events that are currently displayed on the screen
     */
    private List<CompletedEventDisplay> getDisplayedEvents(){
        if (useHabit.isChecked() && filterHabit != null){
            return user.getHabitHistory(filterHabit);
        } else if (!filterText.equals("")) {
            return user.getHabitHistory(filterText);
        } else {
            return user.getHabitHistory();
        }
    }

    /**
     * Convert all displayed habit events into a string representation and update the listview
     */
    private void convertEventsToString(){
        List<CompletedEventDisplay> events = getDisplayedEvents();
        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.list_item, events);
        historyList.setAdapter(adapter);
    }

    /**
     * Handle return values from an activity once it has finished
     * @param requestCode the activity's identifying code
     * @param resultCode what kind of result the activity is returning
     * @param data the bundle of return values from the finished activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                String habitId = data.getStringExtra(FilterHabitsActivity.RETURNED_HABIT_ID);
                if (!habitId.equals("")) {
                    filterHabit = user.getHabitById(habitId);
                    filterHabitText.setText(filterHabit.getTitle());
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

    /**
     * Switch the habit history view type into map view if it is in list view, or into list view if it is in map view
     * @param view
     * @since Version 2
     */
    public void onMapViewClicked(View view){
        // switch into map mode if permission is granted
        requestLocationPermissions();
    }

    /**
     * Handle the results of the request location permission being granted
     */
    @Override
    protected void handleLocationGranted() {
        Intent intent = new Intent(this, ViewEventsMapActivity.class);
        intent.putExtra(ViewEventsMapActivity.ID_EVENTS, (Serializable) getDisplayedEvents());
        startActivity(intent);
    }

    /**
     * Handle the back button being pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ID_PROFILE, user);
        setResult(RESULT_OK, intent);
        finish();
    }

}