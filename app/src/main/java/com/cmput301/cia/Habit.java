package com.cmput301.cia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Oct 14 2017
 */

public class Habit {

    public static final int MONDAY = 2, TUESDAY = 3, WEDNESDAY = 4, THURSDAY = 5, FRIDAY = 6, SATURDAY = 7, SUNDAY = 1;

    private String title;
    private String reason;
    private Date startDate;
    private List<HabitEvent> events;

    // The days of the week this habit should occur on
    private List<Integer> daysOfWeek;

    private int timesMissed;

    /**

     * Construct a new habit object
     * @param title the name of the habit (not null)
     * @param reason the reason prompting the habit (not null)
     * @param startDate the date the user wants to start the habit (not null)
     * @param days the days of the week the event should occur on (Sunday = 1, Saturday = 7)
     */
    public Habit(String title, String reason, Date startDate, List<Integer> days) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        events = new ArrayList<>();
        daysOfWeek = days;
        timesMissed = 0;
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
        return timesMissed;
    }

    /**
     * @return the number of times this habit was completed on time
     */
    public int getTimesCompleted() {
        return events.size();
    }

    /**
     * Increase the number of times this habit was missed
     */
    public void miss(){
        timesMissed++;
    }

    /**
     * @return the most recent event under this habit if exists (null otherwise)
     */
    public HabitEvent getMostRecentEvent(){
        if (events.size() == 0)
            return null;
        return events.get(events.size() - 1);
    }

}
