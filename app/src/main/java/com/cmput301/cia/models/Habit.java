/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 16 2017
 *
 * This class represents a habit that a user has created
 * Keeps track of all habit events the user has completed/missed related to the habit
 */

public class Habit extends ElasticSearchable {

    public static final String TYPE_ID = "habit";

    // Constants for days of the week
    public static final int SUNDAY = 1, MONDAY = 2, TUESDAY = 3, WEDNESDAY = 4, THURSDAY = 5, FRIDAY = 6, SATURDAY = 7;

    // What category this habit falls under
    private String type;

    private String title;
    private String reason;
    private Date startDate;
    // Successfully completed events
    private List<HabitEvent> events;

    // The days of the week this habit should occur on
    private List<Integer> daysOfWeek;

    /**

     * Construct a new habit object
     * @param title the name of the habit (not null)
     * @param reason the reason prompting the habit (not null)
     * @param startDate the date the user wants to start the habit (not null)
     * @param days the days of the week the event should occur on (Sunday = 1, Saturday = 7)
     * @param type the habit type category this habit falls under
     */
    public Habit(String title, String reason, Date startDate, List<Integer> days, String type) {
        this.type = type;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        events = new ArrayList<>();
        daysOfWeek = days;
    }

    /**
     * @return the days of the week this habit occurs on
     */
    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Set the days of the week this habit occurs on
     * @param daysOfWeek days this habit should occur on (Sunday = 1, Saturday = 7)
     */
    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Add a new habit event to the list of successfully completed ones for this habit
     * @param event the event to add
     */
    public void addHabitEvent(HabitEvent event){
        event.setHabitId(getId());
        events.add(event);
    }

    /**
     * Remove a successfully completed habit event
     * @param event the event to remove
     */
    public void removeHabitEvent(HabitEvent event){
        events.remove(event);
    }

    /**
     * @return the habit's name
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the habit's name
     * @param title the new name for the habit
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the reason for creating this habit
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set the reason for creating this habit
     * @param reason reason for creating this habit
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the date at which this habit should be started
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the date at which this habit should be started
     * @param startDate the new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return all successfully completed events falling under this habit
     */
    public List<HabitEvent> getEvents() {
        return events;
    }

    /**
     * @param day the day number of the week (Sunday = 1, Saturday = 7)
     * @return whether this event occurs on the specified day
     */
    public boolean occursOn(int day){
        for (Integer integer : daysOfWeek){
            if (integer == day)
                return true;
        }
        return false;
    }

    /**
     * @return the number of times this habit was missed
     */
    public int getTimesMissed() {
        return getMissedDates().size();
    }

    /**
     * @return the number of times this habit was completed on time
     */
    public int getTimesCompleted() {
        return events.size();
    }

    /**
     * @return the most recent event under this habit if exists (null otherwise)
     */
    public HabitEvent getMostRecentEvent(){
        if (events.size() == 0)
            return null;
        return events.get(events.size() - 1);
    }

    /**
     * @return the dates that this habit was missed
     */
    public List<Date> getMissedDates(){

        List<Date> missedDates = new ArrayList<>();
        Date current = startDate;
        Date today = new Date();

        // the habit has not started yet
        if (DateUtilities.isBefore(today, current))
            return missedDates;

        List<Date> completedDates = new ArrayList<>();
        for (HabitEvent event : events)
            completedDates.add(event.getDate());

        Collections.sort(completedDates);

        Calendar calendar = new GregorianCalendar();
        // go through each day this habit was meant to be completed, and see if it was on that day
        while (!DateUtilities.isSameDay(current, today)){
            calendar.setTime(current);
            if (occursOn(calendar.get(Calendar.DAY_OF_WEEK)) && Collections.binarySearch(completedDates, current) < 0){
                missedDates.add(current);
            }
            calendar.add(Calendar.DATE, 1);
            current = calendar.getTime();
        }
        return missedDates;
    }

    /**
     * @return the last date this habit was succesfully completed on (null if there was none)
     */
    public Date getLastCompletionDate(){
        if (events.size() == 0)
            return null;

        // TODO: if events can have date set use the below line, otherwise looping is necessary
        //return events.get(events.size() - 1).getDate();

        Date lastCompletion = null;
        for (HabitEvent event : events){
            if (lastCompletion == null || lastCompletion.before(event.getDate()))
                lastCompletion = event.getDate();
        }
        return lastCompletion;
    }

    /**
     * Set the habit's type category
     * @param typeStr the category
     */
    public void setType(String typeStr){ //edit by guanfang
        this.type = typeStr;
    }

    /**
     * @return the habit's type category
     */
    public String getType(){return this.type;}

    /**
     * @return the object's template type id
     */
    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    /**
     * Serialize this object to the ElasticSearch server
     * @return whether the save attempt was successful
     */
    @Override
    public boolean save() {

        boolean success = true;

        for (HabitEvent event : events)
            success = success && event.save();

        success = success && ElasticSearchUtilities.save(this);
        return success;
    }

    /**
     * Synchronize this object from the ElasticSearch server
     */
    @Override
    public void load() {
        Habit found = ElasticSearchUtilities.getObject(getTypeId(), Habit.class, getId()).first;
        if (found != null){
            copyFrom(found);
        }
    }

    /**
     * Delete this object from the ElasticSearch server
     */
    @Override
    public void delete() {
        for (HabitEvent event : events)
            event.delete();
        ElasticSearchUtilities.delete(this);
    }

    @Override
    public String toString(){
        return title;
    }

    /**
     * Copy from anotehr habit
     * @param other the habit to copy from
     */
    public void copyFrom(Habit other){
        type = other.type;
        title = other.title;
        reason = other.reason;
        startDate = other.startDate;
        events = other.events;
        daysOfWeek = other.daysOfWeek;
    }

    /**
     * @param event the event to check whether this habit contains already or not
     * @return whether this habit contains the specified event
     */
    public boolean hasEvent(HabitEvent event){
        for (HabitEvent habitEvent : events){
            if (habitEvent.equals(event))
                return true;
        }
        return false;
    }

    /**
     * Edit an existing event
     * @param event the new data an existing event will be given
     */
    public void editEvent(HabitEvent event){
        for (HabitEvent habitEvent : events){
            if (habitEvent.equals(event)){
                habitEvent.copyFrom(event);
            }
        }
    }

    /**
     * @return what percentage of the time this habit is being successfully completed as a string in the form (X.YZ%)
     */
    public String getCompletionPercent(){
        int success = events.size();
        int misses = getTimesMissed();
        if (misses == 0 && success == 0){
            return "0.00%";
        } else {
            double ratio = ((double) success) / ((double) (misses + success)) * 100;
            NumberFormat formatter = new DecimalFormat("#0.00");
            return formatter.format(ratio) + "%";
        }
    }

}
