package com.cmput301.cia;

import android.media.Image;

import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 */

public class HabitEvent {

    String comment;
    Image photo;
    Date date;

    /**
     * Construct a new habit event
     * @param comment the optional habit comment (not null)
     * @param image a photo of the event
     */
    public HabitEvent(String comment, Image image){
        this.comment = comment;
        photo = image;
        date = new Date();
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
}
