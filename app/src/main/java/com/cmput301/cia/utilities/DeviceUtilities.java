package com.cmput301.cia.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.cmput301.cia.models.Profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 09 2017
 *
 * Contains various device related utility functions accessibly anywhere
 */

public class DeviceUtilities {

    // The returned location string when a location is invalid or an error occurs trying to find it's name
    public static final String NO_LOCATION_NAME = "NONE";

    private static LocationListenerImpl listener = new LocationListenerImpl();
    /**
     * dummy class used just for getting the current location
     */
    private static class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    /**
     * @return whether the user is connected to the internet or not
     */
    /*public static boolean isOnline() {
        return ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, new HashMap<String, String>()).size() > 0;
    }*/

    /**
     * PRECONDITION: The user has granted the system permission to access their device's location. May crash otherwise
     * @return the user's last known location if known, or null otherwise
     */
    // TODO: test on a device where GPS works, as the location on Nexus 4 always returns Mountain View
    @SuppressWarnings("MissingPermission")
    public static Location getLocation(Activity activity) {

        /**
         * Reference:
         * https://github.com/googlemaps/android-samples/blob/master/tutorials/CurrentPlaceDetailsOnMap
         */

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return null;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, listener);
        final Location[] location = {null};
        location[0] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Get the device's current location
        /*FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);
        Task<Location> locationResult = client.getLastLocation();
        locationResult.addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // non-null location
                if (task.isSuccessful() && task.getResult() != null) {
                    location[0] = task.getResult();

                }
            }
        });*/

        return location[0];
    }

    /**
     * PRECONDITION: The user has granted the system permission to access their device's location. May crash otherwise
     * @param activity the activity requesting the location name
     * @return the name of the device's current location if found, or "NONE" otherwise
     */
    public static String getLocationName(Activity activity){

        String name = "NONE";

        Location location = getLocation(activity);
        if (location != null) {
            Geocoder geocoder = new Geocoder(activity);
            try {
                Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                name = address.getThoroughfare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return name;
    }

    /**
     * @param activity the activity requesting the location name
     * @param location the device's current location
     * @return the name of the device's current location if found, or DeviceUtilities.NO_LOCATION_NAME otherwise
     */
    public static String getLocationName(Activity activity, Location location){

        String name = NO_LOCATION_NAME;

        if (location != null) {
            Geocoder geocoder = new Geocoder(activity);
            try {
                Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                name = address.getThoroughfare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return name;
    }

}
