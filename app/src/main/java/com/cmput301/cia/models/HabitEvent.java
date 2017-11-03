/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import android.location.Location;
import android.media.Image;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Version 1.2
 * Author: Adil Malik
 * Modified by: Jessica Prieto
 * Date: Oct 13 2017
 *
 * Represents a habit event that the user has successfully completed
 */

public class HabitEvent extends ElasticSearchable implements Serializable {

    private String comment;
    private String base64EncodedPhoto;
    private Date date;

    private Location location;

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     */
    public HabitEvent(String comment){
        this.comment = comment;
        base64EncodedPhoto = "";
        date = new Date();
        location = null;
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
        location = null;
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
        location = null;
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
        location = null;
    }

    /**
     *
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a base64EncodedPhoto of the event (not null)
     * @param date the date the event occurred on (not null)
     * @param location the location where the event occurred
     */
    public HabitEvent(String comment, String image, Date date, Location location) {
        this.comment = comment;
        this.base64EncodedPhoto = image;
        this.date = date;
        this.location = location;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBase64EncodedPhoto() {
        return base64EncodedPhoto;
    }

    public void setBase64EncodedPhoto(String base64EncodedPhoto) {
        this.base64EncodedPhoto = base64EncodedPhoto;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the object's template type id
     */
    @Override
    public String getTypeId() {
        return "habitevent";
    }

    /**
     * Serialize this object to the ElasticSearch server
     */
    @Override
    public void save() {
        // TODO
    }

    /**
     * Synchronize this object from the ElasticSearch server
     */
    @Override
    public void load() {
        // TODO
    }

    /**
     * Delete this object from the ElasticSearch server
     */
    @Override
    public void delete() {
        // TODO
    }
}
