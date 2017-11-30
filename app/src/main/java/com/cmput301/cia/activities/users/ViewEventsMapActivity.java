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
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 29, 2017
 *
 * This activity allows the user to view a list of events that have locations with highlighted markers on a map
 */

public class ViewEventsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    // Intent identifier for incoming events list
    public static final String ID_EVENTS = "Events";

    private GoogleMap mMap;

    // the list of events that could be displayed on the map
    private List<CompletedEventDisplay> events;

    private LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_events);

        events = (List<CompletedEventDisplay>) getIntent().getSerializableExtra(ID_EVENTS);

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
        builder = new LatLngBounds.Builder();

        // whether there is atleast one event with a location to be displayed on the map or not
        boolean atleastOneEvent = false;

        for (CompletedEventDisplay event : events){
            Location location = event.getLocation();
            if (location != null){
                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                builder.include(coordinates);
                atleastOneEvent = true;
                mMap.addMarker(new MarkerOptions().position(coordinates).title(event.getDescriptionWithLocation(this)));
            }
        }

        /**
         * This, along with the LatLngBounds.Builder part of this method is based off of andr's answer:
         * https://stackoverflow.com/a/14828739
         *
         * The width, height, padding aspects of newLatLngBounds() is from:
         * https://github.com/OneBusAway/onebusaway-android/issues/581
         */
        // TODO: test on other devices to make sure diff sizes work, test with different markers far away
        if (atleastOneEvent) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDisplayMetrics().widthPixels,
                    getResources().getDisplayMetrics().heightPixels, (int) Math.ceil(0.12 * getResources().getDisplayMetrics().widthPixels)));
        } else {
            // move the map to the device's current location
            Location deviceLoc = DeviceUtilities.getLocation(this);
            if (deviceLoc != null)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(deviceLoc.getLatitude(), deviceLoc.getLongitude()), 50));
        }
    }
}
