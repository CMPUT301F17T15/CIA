/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.models.GeoLocationEvent;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;



/**
 * Created by Dinesh on Oct 22 2017.
 * Version 1
 *
 *
 */

public class GelocationUnitTests extends ActivityInstrumentationTestCase2 {

    public GelocationUnitTests() {
        super(HabitEvent.class);
    }

    public void testGeoLocation(){
        GeoLocationEvent belize = new GeoLocationEvent(17.1899,88.4976);
        Location location = new Location("belize");
        location.setLatitude(17.1899d);
        location.setLongitude(88.4976d);
        assertTrue(location.equals(belize));
    }
    public void testLocation(){
        GeoLocationEvent belize = new GeoLocationEvent(17.1899,88.4976);
        GeoLocationEvent newlocation = new GeoLocationEvent(53.525283,-113.525612);
        Location location = new Location("belize");
        assertTrue(location.equals(newlocation));
    }
}
