/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 24, 2017
 *
 * This activity allows the user to view a list of events that have locations with highlighted markers on a map
 */

public class ViewEventsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String ID_EVENTS = "Events";

    private GoogleMap mMap;

    // the list of events that could be displayed on the map
    private List<HabitEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_events);

        events = (List<HabitEvent>) getIntent().getSerializableExtra(ID_EVENTS);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Add map markers for all events that have valid locations
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (HabitEvent habitEvent : events){
            Location location = habitEvent.getLocation();
            if (location != null){
                Pair<Habit, Boolean> habit = ElasticSearchUtilities.getObject(Habit.TYPE_ID, Habit.class, habitEvent.getHabitId());
                String habitTitle = habit.first == null ? "habit" : habit.first.getTitle();

                if (habit.first == null)
                    Toast.makeText(this, "Connection to the database was lost. Can't access habit name.", Toast.LENGTH_LONG).show();

                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(coordinates).title("Completed " + habitTitle + " at " + DeviceUtilities.getLocationName(this, location)));
            }
            //mMap.addMarker(new MarkerOptions().position())
        }

        // move the map to the device's current location
        // TODO: zoom out the map to show all marked locations
        Location deviceLoc = DeviceUtilities.getLocation(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(deviceLoc.getLatitude(), deviceLoc.getLongitude()), 50));
    }
}
