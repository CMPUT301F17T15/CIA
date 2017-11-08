package com.cmput301.cia.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

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
    public static Location getLocation(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //activity.requestPermissions(PermissionInfo.);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        // TODO
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, listener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }

}
