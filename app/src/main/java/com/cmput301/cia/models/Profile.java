/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.SerializableUtilities;

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
 * Version 2
 * Author: Adil Malik
 * Date: Oct 14 2017
 */

public class Profile extends ElasticSearchable {

    public static final String TYPE_ID = "profile";

    private String name;
    private List<Habit> habits;

    // list of users this user is following (unique)
    private List<Profile> following;

    // users that have requested to follow this user (unique)
    private List<Profile> followRequests;

    // Events that will be synchronized when the user signs in on a valid connection
    private List<OfflineEvent> pendingEvents;

    /**
     * Construct a new user profile object
     * @param name the name of the user (not null)
     */
    public Profile(String name) {
        this.name = name;
        habits = new ArrayList<>();
        following = new ArrayList<>();
        followRequests = new ArrayList<>();
        pendingEvents = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public List<Profile> getFollowing() {
        return following;
    }

    public void addHabit(Habit habit){
        habits.add(habit);
    }

    public void removeHabit(Habit habit){
        habits.remove(habit);
    }

    /**
     * Add the specified user to the list of people this user is following
     * @param profile the user to follow
     */
    public void follow(Profile profile){
        following.add(profile);
    }

    /**
     * Add a follow request from the specified user
     * @param profile the user sending the request
     */
    public void addFollowRequest(Profile profile){
        followRequests.add(profile);
    }

    /**
     * @param profile the user to check if they sent a follow request
     * @return whether the specified user has requested to follow this user
     */
    public boolean hasFollowRequest(Profile profile){
        return followRequests.contains(profile);
    }

    /**
     * Remove the follow request from the specified user
     * @param profile the user sending the request
     */
    public void removeFollowRequest(Profile profile){
        followRequests.remove(profile);
    }

    /**
     * @return a list of habits that need to be done today
     */
    public List<Habit> getTodaysHabits() {
        return getTodaysHabits(new Date());
    }

    /**
     * @param date is the day that habits need to be done on
     * @return a list of habits that need to be done on the specified day
     */
    public List<Habit> getTodaysHabits(Date date){
        List<Habit> list = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        for (Habit habit : habits){
            if (habit.occursOn(today))
                list.add(habit);
        }

        return list;
    }

    /**
     * @return the user's habit history, sorted in descending order based on date
     */
    public List<HabitEvent> getHabitHistory(){
        List<HabitEvent> list = new ArrayList<>();
        for (Habit habit : habits){
            list.addAll(habit.getEvents());
        }
        Collections.sort(list, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent event, HabitEvent t1) {
                return event.getDate().compareTo(t1.getDate());     // TODO: test correctness
            }
        });
        return list;
    }

    /**
     * @param filter phrase that every event included in the list must contain
     * @return the user's filtered habit history, sorted in descending order based on date
     */
    public List<HabitEvent> getHabitHistory(String filter){
        List<HabitEvent> list = new ArrayList<>();
        for (Habit habit : habits){
            for (HabitEvent event : habit.getEvents()) {
                if (event.getComment().contains(filter))
                    list.add(event);
            }
        }
        Collections.sort(list, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent event, HabitEvent t1) {
                return event.getDate().compareTo(t1.getDate());     // TODO: test correctness
            }
        });
        return list;
    }

    /**
     * @param filter habit that every event included in the list must be based off of
     * @return the user's filtered habit history, sorted in descending order based on date
     */
    public List<HabitEvent> getHabitHistory(Habit filter){
        List<HabitEvent> list = null;
        for (Habit habit : habits){
            // TODO: is for loop necessary, or can filter.getEvents() be used instead?
            if (habit.equals(filter)){
                list = habit.getEvents();

                Collections.sort(list, new Comparator<HabitEvent>() {
                    @Override
                    public int compare(HabitEvent event, HabitEvent t1) {
                        return event.getDate().compareTo(t1.getDate());     // TODO: test correctness
                    }
                });

                break;
            }
        }

        if (list == null)
            list = new ArrayList<>();
        return list;
    }

    /**
     * @return list of the most recent event for each habit of all followed users, sorted by user name and then habit title
     */
    public List<HabitEvent> getFollowedHabitHistory(){
        List<HabitEvent> list = new ArrayList<>();

        for (Profile followee : following) {
            for (Habit habit : followee.getHabits()) {
                HabitEvent event = habit.getMostRecentEvent();
                if (event != null)
                    list.add(event);
            }

            // TODO: an efficient algorithm that works
            // maybe use List<Tuple<String, String, HabitEvent>> and then extract the HabitEvent
            Collections.sort(list, new Comparator<HabitEvent>() {
                @Override
                public int compare(HabitEvent event, HabitEvent t1) {
                    return event.getDate().compareTo(t1.getDate());
                }
            });
        }
        return list;
    }

    /**
     * @return the number of habits this profile has
     */
    public int getHabitsCount(){
        return habits.size();
    }

    /**
     * After a day is finished, update the status of all uncompleted habits
     * @param endingDay is the day that ended
     */
    public void onDayEnd(Date endingDay){
        // TODO
        // look through all uncompleted habits on endingDay (use getTodaysHabits(endingDay))
        // make sure to account for start daet later than current date
    }

    /**
     * Saves all pending events to the database.
     * Call this when this user is signed in and regains connection to the internet.
     */
    public void synchronize(){
        if (DeviceUtilities.isOnline()){
            List<OfflineEvent> unsuccessful = new ArrayList<>();
            for (OfflineEvent event : pendingEvents){
                if (!event.handle()){
                    unsuccessful.add(event);
                }
            }
            pendingEvents = unsuccessful;
        }
    }

    /**
     * Attempt to edit/delete an existing habit event, or add a new one
     * @param event represents the object that handles what needs to be done
     */
    public void tryHabitEvent(OfflineEvent event){
        if (DeviceUtilities.isOnline() && event.handle()){
            // do nothing
        }
        else
            pendingEvents.add(event);
    }

    /**
     * @param profile the user that is being checked to see if this user follows
     * @return whether this user is following the specified profile
     */
    public boolean isFollowing(Profile profile){
        return following.contains(profile);
    }

    /**
     * @return the object's template type id
     */
    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    /**
     * Save this profile to the database
     */
    public void save(){
        ElasticSearchUtilities.save(this);
        for (Habit habit : habits){
            ElasticSearchUtilities.save(habit);
        }
        SerializableUtilities.save(getOfflineEventsFile(), pendingEvents);
    }

    /**
     * Synchronize this object from the ElasticSearch server
     */
    @Override
    public void load() {
        Profile found = ElasticSearchUtilities.getObject(getTypeId(), Profile.class, getId());
        if (found != null){
            Map<String, String> params = new HashMap<>();
            params.put("creator", getId());
            List<Habit> foundHabits = ElasticSearchUtilities.getListOf(Habit.TYPE_ID, Habit.class, params);

            // TODO: copy from vars into this
        }

        List<OfflineEvent> loaded = SerializableUtilities.load(getOfflineEventsFile());
        if (loaded != null)
            pendingEvents = loaded;
    }

    /**
     * Delete this object from the ElasticSearch server
     */
    @Override
    public void delete() {
        for (Habit habit : habits)
            ElasticSearchUtilities.delete(habit);
        ElasticSearchUtilities.delete(this);
    }

    /**
     * @return the name of the file containing this user's pending events
     */
    private String getOfflineEventsFile(){
        return name + "events.sav";
    }
}
