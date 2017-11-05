package com.cmput301.cia;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 */

public class HabitEvent {

    private String comment;
    private Image photo;
    private Date date;

    private Location location;

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a photo of the event
     */
    public HabitEvent(String comment, Image image){
        this.comment = comment;
        photo = image;
        date = new Date();
        location = null;
    }

    /**
     * Construct a new habit event
     * @param comment the optional habit comment
     * @param image a photo of the event
     * @param date the date the event occurred on
     */
    public HabitEvent(String comment, Image image, Date date){
        this.comment = comment;
        photo = image;
        this.date = date;
        location = null;
    }

    /**
     *
     * Construct a new habit event
     * @param comment the optional habit comment
     * @param photo a photo of the event
     * @param date the date the event occurred on
     * @param location the location where the event occurred
     */
    public HabitEvent(String comment, Image photo, Date date, Location location) {
        this.comment = comment;
        this.photo = photo;
        this.date = date;
        this.location = location;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
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
}
