package com.cmput301.cia.utilities;

import android.location.Location;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 *
 * Contains various device related utility functions accessibly anywhere
 */

public class DeviceUtilities {

    /**
     * @return whether the user is connected to the internet or not
     */
    public static boolean isOnline(){
        return true;
    }

    /**
     * @return the user's last known location if known, or null otherwise
     */
    public static Location getLocation(){
        return null;
    }

}
