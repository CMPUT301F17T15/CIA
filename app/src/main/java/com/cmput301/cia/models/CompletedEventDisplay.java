/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import android.app.Activity;
import android.location.Location;

import com.cmput301.cia.utilities.LocationUtilities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 29 2017
 *
 * This class contains information needed for displaying a habit event, such as the containing habit name and the name of the user
 * who completed it. Used for consistent formatting when displaying a habit event
 */

public class CompletedEventDisplay implements Serializable {

    private HabitEvent event;
    private String habitName;
    private String userName;

    /**
     * Construct a new CompletedEventDisplay object
     * Used for displaying other user's events
     * @param event the event to display details about
     * @param habitName the name of the habit the event is a completion of
     * @param userName the username of the person completing the event
     */
    public CompletedEventDisplay(HabitEvent event, String habitName, String userName){
        this.event = event;
        this.habitName = habitName;
        this.userName = userName;
    }

    /**
     * Construct a new CompletedEventDisplay object
     * Used for displaying the signed in user's events
     * @param event the event to display details about
     * @param habitName the name of the habit the event is a completion of
     */
    public CompletedEventDisplay(HabitEvent event, String habitName){
        this.event = event;
        this.habitName = habitName;
        this.userName = "";
    }

    /**
     * Format the event for display in a list
     * @return a formatted display of the event
     */
    @Override
    public String toString(){
        DateFormat df = SimpleDateFormat.getDateInstance();
        if (userName.equals("")){
            return "Completed " + habitName + " on " + df.format(getCompletionDate());
        } else {
            return userName + " completed " + habitName + " on " + df.format(getCompletionDate());
        }
    }

    /**
     * Get a description of this event, taking into account the location it was completed at
     * Assumes that the device has granted permission
     * @param activity the activity requesting this description
     * @return a description with the location if it has a valid location. Otherwise, equivalent of calling toString()
     */
    public String getDescriptionWithLocation(Activity activity){
        Location location = getLocation();
        String locationName = LocationUtilities.getLocationName(activity, getLocation());
        if (location.equals(LocationUtilities.NO_LOCATION_NAME)){
            return toString();
        } else {
            if (userName.equals("")){
                return "Completed " + habitName + " at " + locationName;
            } else {
                return userName + " completed " + habitName + " at " + locationName;
            }
        }
    }

    /**
     * @return the completed habit event
     */
    public HabitEvent getEvent() {
        return event;
    }

    /**
     * @return the date the event was completed on
     */
    public Date getCompletionDate(){
        return event.getDate();
    }

    /**
     * @return the location the event was completed at, or null if none was attached
     */
    public Location getLocation(){
        return event.getLocation();
    }

    /**
     * @return the name of the habit this event is a completion of
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * @param other the object to compare this object with
     * @return whether this object equals another object
     */
    public boolean equals(Object other){
        if (other instanceof CompletedEventDisplay){
            CompletedEventDisplay cedOther = (CompletedEventDisplay)other;
            return event.equals(cedOther.event) && habitName.equals(cedOther.habitName) && userName.equals(cedOther.userName);
        }

        return false;
    }

}
