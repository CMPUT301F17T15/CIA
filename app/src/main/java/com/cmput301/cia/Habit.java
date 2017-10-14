package com.cmput301.cia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 08 2017
 */

public class Habit {

    String title;
    String reason;
    Date startDate;
    List<HabitEvent> events;

    /**
     * Construct a new habit object
     * @param title the name of the habit (not null)
     * @param reason the reason prompting the habit (not null)
     * @param startDate the date the user wants to start the habit (not null)
     */
    public Habit(String title, String reason, Date startDate) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        events = new ArrayList<>();
    }

    public void addHabitEvent(HabitEvent event){
        events.add(event);
    }

    public void removeHabitEvent(HabitEvent event){
        events.remove(event);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<HabitEvent> getEvents() {
        return events;
    }
}
