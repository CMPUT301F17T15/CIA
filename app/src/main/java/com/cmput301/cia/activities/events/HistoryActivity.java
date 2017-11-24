/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.LocationRequestingActivity;
import com.cmput301.cia.models.DeleteHabitEvent;
import com.cmput301.cia.models.EditHabitEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 16 2017
 *
 * This activity allows the user to view all of their completed habit events
 */

public class HistoryActivity extends LocationRequestingActivity {

    // Intent data identifier for the passed in profile
    public static final String ID_PROFILE = "Profile";

    // Intent data identifier for returned habits list
    public static final String RETURNED_HABITS_ID = "Habits";

    private static final int FILTER_CODE = 0, EVENT_CODE = 1;

    // the list of habits that are currently displayed on the screen
    private ListView historyList;

    private EditText filterEditText;
    private CheckBox useHabit;
    private TextView filterHabitText;
    private MapView map;

    // The user who is viewing their habit history
    private Profile user;

    // The habit that is selected as a filter
    private Habit filterHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        user = (Profile) getIntent().getSerializableExtra(ID_PROFILE);
        filterHabit = null;

        historyList = (ListView) findViewById(R.id.historyList);
        filterEditText = (EditText) findViewById(R.id.filterEditText);
        useHabit = (CheckBox)findViewById(R.id.historyTypeCheckbox);
        filterHabitText = (TextView)findViewById(R.id.historyFilterHabitText);
        map = (MapView)findViewById(R.id.historyMapView);
        map.onCreate(savedInstanceState);

        Button historyReturnButton  = (Button) findViewById(R.id.historyReturnButton);
        Button eventButton = (Button) findViewById(R.id.historyEventButton);
        Button filter = (Button) findViewById(R.id.historyFilterButton);

        // allow the user to pick a filter habit
        eventButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, FilterEventsActivity.class);
                intent.putExtra(FilterEventsActivity.ID_USER, user.getId());
                startActivityForResult(intent, FILTER_CODE);
            }
        });

        // apply filter
        filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (useHabit.isChecked() && filterHabit == null){
                    Toast.makeText(HistoryActivity.this, "No filter habit was selected. Checkbox is ignored.", Toast.LENGTH_SHORT).show();
                }
                convertEventsToString();
                updateMap();
            }
        });

        // return to main menu
        historyReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(RETURNED_HABITS_ID, (Serializable) user.getHabits());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // view the details of a habit event
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
     * Convert all displayed habit events into a string representation and update the listview
     */
    private void convertEventsToString(){
        List<HabitEvent> events = getDisplayedEvents();
        List<String> habitList = new ArrayList<>(events.size());

        for (HabitEvent event : events) {
            Habit habit = user.getHabitById(event.getHabitId());
            habitList.add("Completed " + habit.getTitle() + " on " + event.getDate());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, habitList);
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
                String habitId = data.getStringExtra(FilterEventsActivity.RETURNED_HABIT_ID);
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
     * Update the markers displayed on the map
     */
    // TODO: zoom out the map to show all marked locations
    private void updateMap(){
        map.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();              // remove all markers from the map
                // add marker for all habit events with locations
                for (HabitEvent habitEvent : getDisplayedEvents()){
                    Location location = habitEvent.getLocation();
                    if (location != null){
                        Pair<Habit, Boolean> habit = ElasticSearchUtilities.getObject(Habit.TYPE_ID, Habit.class, habitEvent.getHabitId());
                        String habitTitle = habit.first == null ? "" : habit.first.getTitle();
                        LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(coordinates).title("Completed " + habitTitle + " at " + DeviceUtilities.getLocationName(HistoryActivity.this, location)));
                    }
                }

                // move the map to the device's current location
                Location deviceLoc = DeviceUtilities.getLocation(HistoryActivity.this);
                if (deviceLoc == null){
                    deviceLoc = DeviceUtilities.getLocation(HistoryActivity.this);
                }
                // try to get the location a second time
                if (deviceLoc == null){
                    deviceLoc = DeviceUtilities.getLocation(HistoryActivity.this);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(deviceLoc.getLatitude(), deviceLoc.getLongitude()), 50));
                map.onResume();
            }
        });

    }

    /**
     * Switch the habit history view type into map view if it is in list view, or into list view if it is in map view
     * @param view
     * @since Version 2
     */
    public void onMapViewClicked(View view){
        // switch into map mode
        if (historyList.getVisibility() == View.VISIBLE){
            requestLocationPermissions();
        } else {    // switch into list mode
            historyList.setVisibility(View.VISIBLE);
            map.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Handle the results of the request location permissions
     * @param granted whether permission was granted or not to use the user's location
     */
    @Override
    public void handleLocationResponse(boolean granted) {
        if (granted){
            historyList.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            updateMap();
        } else {
            Toast.makeText(this, "Can not access map view without granting permission", Toast.LENGTH_SHORT).show();
        }
    }


}