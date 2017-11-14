/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import android.location.Location;
import android.location.LocationManager;

import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.io.Serializable;
import java.util.Date;

/**
 * Version 2
 * Author: Adil Malik
 * Modified by: Jessica Prieto
 * Date: Nov 11 2017
 *
 * Represents a habit event that the user has successfully completed
 * Keeps track of all information regarding the habit event
 */

public class HabitEvent extends ElasticSearchable {

    // if the latitude/longitude variable is equal to this, then the location is null
    private static final double INVALID_LATLONG = -999999999999999.259;

    public static final String TYPE_ID = "habitevent";

    private String comment;
    private String base64EncodedPhoto;
    private Date date;

    private double latitude;
    private double longitude;

    // the ID of the habit that contains this event
    private String habitId;

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     */
    public HabitEvent(String comment){
        this.comment = comment;
        base64EncodedPhoto = "";
        date = new Date();
        latitude = INVALID_LATLONG;
        longitude = INVALID_LATLONG;
    }

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param date the date the event occurred on (not null)
     */
    public HabitEvent(String comment, Date date){
        this.comment = comment;
        base64EncodedPhoto = "";
        this.date = date;
        latitude = INVALID_LATLONG;
        longitude = INVALID_LATLONG;
    }

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a base64 encoded photo of the event (not null)
     */
    public HabitEvent(String comment, String image){
        this.comment = comment;
        base64EncodedPhoto = image;
        date = new Date();
        latitude = INVALID_LATLONG;
        longitude = INVALID_LATLONG;
    }

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a base64 encoded photo of the event (not null)
     * @param date the date the event occurred on (not null)
     */
    public HabitEvent(String comment, String image, Date date){
        this.comment = comment;
        base64EncodedPhoto = image;
        this.date = date;
        latitude = INVALID_LATLONG;
        longitude = INVALID_LATLONG;
    }

    /**
     *
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a base64EncodedPhoto of the event (not null)
     * @param date the date the event occurred on (not null)
     * @param latitude the latitude of the location where the event occurred
     * @param longitude the longitude of the location where the event occurred
     */
    public HabitEvent(String comment, String image, Date date, double latitude, double longitude) {
        this.comment = comment;
        this.base64EncodedPhoto = image;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the comment about this event
     */
    public String getComment(){
        return comment;
    }

    /**
     * Set the event's description
     * @param comment the event's new description
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the image about the event encoded in base64
     */
    public String getBase64EncodedPhoto() {
        return base64EncodedPhoto;
    }

    /**
     * Set the image about this event
     * @param base64EncodedPhoto the image about the event encoded in base64
     */
    public void setBase64EncodedPhoto(String base64EncodedPhoto) {
        this.base64EncodedPhoto = base64EncodedPhoto;
    }

    /**
     * @return the date this event occurred on
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date this event occurred on
     * @param date the date this event occurred on
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the location this event occurred at if it exists, or null otherwise
     */
    public Location getLocation() {

        if (latitude == INVALID_LATLONG)
            return null;

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

    /**
     * Set the location this event occurred at
     * @param location the location it occurred at (can be null)
     */
    public void setLocation(Location location) {

        if (location == null){
            latitude = INVALID_LATLONG;
            longitude = INVALID_LATLONG;
            return;
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    /**
     * @return the object's template type id
     */
    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    /**
     * Serialize this object to the ElasticSearch server
     */
    @Override
    public void save() {
        ElasticSearchUtilities.save(this);
    }

    /**
     * Synchronize this object from the ElasticSearch server
     */
    @Override
    public void load() {
        HabitEvent found = ElasticSearchUtilities.getObject(getTypeId(), HabitEvent.class, getId());
        if (found != null){
            copyFrom(found);
        }
    }

    /**
     * Delete this object from the ElasticSearch server
     */
    @Override
    public void delete() {
        ElasticSearchUtilities.delete(this);
    }

    /**
     * Copy over the data from another habit event into this object
     * @param other the object to copy from (not null)
     */
    public void copyFrom(HabitEvent other){
        comment = other.comment;
        base64EncodedPhoto = other.base64EncodedPhoto;
        date = other.date;
        latitude = other.latitude;
        longitude = other.longitude;
    }

    /**
     * Set the id of the habit containing this event
     * @param habitId the id of the habit containing this event
     */
    public void setHabitId(String habitId){
        this.habitId = habitId;
    }

    /**
     * @return the id of the habit containing this event
     */
    public String getHabitId(){
        return habitId;
    }

}
