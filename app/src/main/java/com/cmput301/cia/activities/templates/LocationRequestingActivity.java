/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.templates;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cmput301.cia.R;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 23 2017
 *
 * This abstract activity represents the base for any activity that requests the user for permission
 * to use their device's location
 * Follows the template method pattern
 */

public abstract class LocationRequestingActivity extends AppCompatActivity {

    // unique code for requesting the location permission request
    private static final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_requesting);
    }

    /**
     * Handle the results of requesting the user's permission to do something
     * @param requestCode the request code of the permission
     * @param permissions the list of permissions that were requested
     * @param grantResults the list of results, where each element at index i corresponds to permissions[i]
     *
     * Reference: https://developer.android.com/training/permissions/requesting.html
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                handleLocationPermissionRequest(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                return;
            }
        }
    }

    /**
     * Handle the results of a request for location permissions
     * @param granted whether permission was granted or not
     */
    private void handleLocationPermissionRequest(boolean granted){
        if (granted){
            handleLocationGranted();
        } else {
            Toast.makeText(this, "Some features are unavailable without location permissions", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Request the permission to use the user's device location
     * Note: the results should be handled by overriding handleLocationGranted()
     */
    protected void requestLocationPermissions(){
        // Request permissions for the GPS service if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            handleLocationGranted();
        }
    }

    /**
     * Handle the results of the request location permission being granted
     */
    protected abstract void handleLocationGranted();

}
