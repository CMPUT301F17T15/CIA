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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/*import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
*/

import java.io.IOException;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 *
 * Contains various device related utility functions accessibly anywhere
 */

public class DeviceUtilities {

    private static LocationListenerImpl listener = new LocationListenerImpl();

    private static class LocationListenerImpl implements LocationListener{

        private Location lastLoc = null;

        @Override
        public void onLocationChanged(Location location) {
            lastLoc = location;
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
    public static boolean isOnline() {
        return true;
    }

    /**
     * @return the user's last known location if known, or null otherwise
     */
    // TODO: test Google API on an emulator device that works (requires a Google Play update so it won't work on Nexus 4)
    public static Location getLocation(Activity activity) {

        /**
         * Reference:
         * https://github.com/googlemaps/android-samples/blob/master/tutorials/CurrentPlaceDetailsOnMap
         */

        // Request permissions for the GPS service if not granted
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
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
                if (task.isSuccessful()) {
                    location[0] = task.getResult();
                    System.out.println("LOCLOCLOCLOC= " + location[0].toString());
                }
            }
        });*/

        return location[0];
    }

    /**
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
     * @return the name of the device's current location if found, or "NONE" otherwise
     */
    public static String getLocationName(Activity activity, Location location){

        String name = "NONE";

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
