package com.cmput301.cia.utilities;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 09 2017
 *
 * Contains various device location related utility functions accessible anywhere
 */

public class LocationUtilities {

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
     * PRECONDITION: The user has granted the system permission to access their device's location. May crash otherwise
     * @return the user's last known location if known, or null otherwise
     */
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
        return location[0];
    }

    /**
     * @param activity the activity requesting the location name
     * @param location the device's current location
     * @return the name of the device's current location if found, or LocationUtilities.NO_LOCATION_NAME otherwise
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
