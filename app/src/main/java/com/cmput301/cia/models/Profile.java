/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import android.location.Location;

import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.SerializableUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Adil Malik
 * @version 7
 * Date: Nov 19 2017
 *
 * This class represents all the information about a user of the application.
 * It keeps track of their habits and personal information.
 *
 * @see Habit
 * @see HabitEvent
 * @see OfflineEvent
 */

public class Profile extends ElasticSearchable {

    public static final String TYPE_ID = "profile";

    // The absolute maximum value for any user's powerPoints attribute
    private static final int MAX_PP = 1000;

    // The user's unique name
    private String name;

    // The user's list of created habits
    private List<Habit> habits;

    // List of users this user is following (all elements are unique)
    private List<Profile> following;

    // Users that have requested to follow this user (all elements are unique)
    private List<Profile> followRequests;

    // Points received for consecutively completing habits
    private int powerPoints;

    // Points received for completing a habit
    private int habitPoints;

    // The date this profile was created
    private Date creationDate;

    // The user's comment about their profile
    private String comment;

    // The last time this user logged in
    private Date lastLogin;

    // The user's profile picture in base64
    private String image;

    /**
     * Construct a new user profile object
     * @param name the name of the user (not null)
     */
    public Profile(String name) {
        this.name = name;
        habits = new ArrayList<>();
        following = new ArrayList<>();
        followRequests = new ArrayList<>();
        powerPoints = 0;
        habitPoints = 0;
        creationDate = new Date();
        comment = new String();
        lastLogin = new Date();
        image = new String();
    }

    /**
     * @return the profile's unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the profile's unique name
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the user's list of habits
     */
    public List<Habit> getHabits() {
        return habits;
    }

    /**
     * @return list of all users this user is following
     */
    public List<Profile> getFollowing() {
        return following;
    }

    /**
     * Add a new habit to the user's list of habits
     * @param habit the habit to add
     */
    public void addHabit(Habit habit){
        habits.add(habit);
    }

    /**
     * Remove a habit from the user's list of habits
     * @param habit the habit to remove
     */
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

        // the requester is already following this user
        if (profile.isFollowing(this))
            return;

        if (!hasFollowRequest(profile))
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
     * Accept a follow request from the specified user
     * @param profile the user sending the request
     */
    public void acceptFollowRequest(Profile profile){
        profile.follow(this);
        removeFollowRequest(profile);
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
            // don't include habits that haven't begun yet
            if (habit.occursOn(today) && !date.before(habit.getStartDate()))
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
                return -1 * event.getDate().compareTo(t1.getDate());
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
                return -1 * event.getDate().compareTo(t1.getDate());
            }
        });
        return list;
    }

    /**
     * @param filter habit that every event included in the list must be based off of
     * @return the user's filtered habit history, sorted in descending order based on date
     */
    public List<HabitEvent> getHabitHistory(Habit filter){
        List<HabitEvent> list = filter.getEvents();
        Collections.sort(list, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent event, HabitEvent t1) {
                return -1 * event.getDate().compareTo(t1.getDate());
            }
        });

        return list;
    }

    /**
     * @return list of the most recent event for each habit of all followed users, sorted by user name and then habit title
     */
    public List<HabitEvent> getFollowedHabitHistory(){
        List<HabitEvent> list = new ArrayList<>();
        // map (event -> [user name of creator, habit title])
        final Map<HabitEvent, String[]> eventDetailsMap = new HashMap<>();

        for (Profile followee : following) {
            for (Habit habit : followee.getHabits()) {
                HabitEvent event = habit.getMostRecentEvent();
                if (event != null) {
                    list.add(event);
                    eventDetailsMap.put(event, new String[]{followee.getName(), habit.getTitle()});
                }
            }
        }

        // TODO: test
        Collections.sort(list, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent eventOne, HabitEvent eventTwo) {

                String[] oneKeys = eventDetailsMap.get(eventOne);
                String[] twoKeys = eventDetailsMap.get(eventTwo);

                // Attempt to compare based on username
                int nameComp = oneKeys[0].compareTo(twoKeys[0]);
                if (nameComp != 0){
                    return nameComp;
                }

                // Compare based on habit title
                return oneKeys[1].compareTo(twoKeys[1]);
            }
        });
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
        List<Habit> toComplete = getTodaysHabits(endingDay);

        for (Habit habit : toComplete){
            Date date = habit.getLastCompletionDate();
            if (date == null || DateUtilities.isBefore(date, endingDay)){
                habit.miss(endingDay);
                powerPoints = 0;
            }
        }

    }

    /**
     * Attempt to edit/delete an existing habit event, or add a new one
     * @param event represents the object that handles what needs to be done
     */
    public void tryHabitEvent(OfflineEvent event){
        event.handle(this);
        save();
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
    @Override
    public void save(){
        for (Habit habit : habits){
            habit.save();
        }
        ElasticSearchUtilities.save(this);
        SerializableUtilities.save(getOfflineEventsFile(), habits);
    }

    /**
     * Synchronize this object from the ElasticSearch server
     */
    @Override
    public void load() {
        Profile found = ElasticSearchUtilities.getObject(getTypeId(), Profile.class, getId()).first;
        if (found != null){
            copyFrom(found);
        }

        List<Habit> loaded = SerializableUtilities.load(getOfflineEventsFile());
        if (loaded != null)
            habits = loaded;
    }

    /**
     * Delete this object from the ElasticSearch server
     */
    @Override
    public void delete() {
        for (Habit habit : habits)
            habit.delete();
        ElasticSearchUtilities.delete(this);
    }

    /**
     * @return the name of the file containing this user's pending events
     */
    private String getOfflineEventsFile(){
        return name + ".sav";
    }

    /**
     * @return the list of habit categories the user has created
     */
    public Set<String> getHabitCategories(){
        Set<String> categories = new HashSet<>();
        for (Habit habit : habits){
            categories.add(habit.getType());
        }
        return categories;
    }

    /**
     * Get a habit by it's unique ID
     * @param habitId the id of the habit
     * @return the habit if found, or null otherwise
     */
    public Habit getHabitById(String habitId){
        for (Habit habit : habits){
            if (habit.getId().equals(habitId))
                return habit;
        }
        return null;
    }

    /**
     * @return the points representing the user's power ranking score
     */
    public int getPowerPoints() {
        return powerPoints;
    }

    /**
     * Set the user's power ranking score
     * @param powerPoints the new score
     */
    public void setPowerPoints(int powerPoints) {
        if (powerPoints > MAX_PP)
            powerPoints = MAX_PP;
        this.powerPoints = powerPoints;
    }

    /**
     * @return the points representing how many habit events the user has completed
     */
    public int getHabitPoints() {
        return habitPoints;
    }

    /**
     * Set the user's habit points
     * @param habitPoints the new habit points
     */
    public void setHabitPoints(int habitPoints) {
        this.habitPoints = habitPoints;
    }

    /**
     * @param location the user's current location
     * @return a list of all habit events (by this user and the most recent event of each type from followers) within 5km of the user's current location
     */
    // TODO: test if distance is correct
    public List<HabitEvent> getNearbyEvents(Location location){
        List<HabitEvent> nearbyEvents = new ArrayList<>();

        if (location != null) {
            final float MAX_DISTANCE = 5000.0f;

            List<HabitEvent> allEvents = getHabitHistory();
            allEvents.addAll(getFollowedHabitHistory());

            for (HabitEvent event : allEvents) {
                Location eventLoc = event.getLocation();
                if (eventLoc != null && eventLoc.distanceTo(location) <= MAX_DISTANCE) {
                    nearbyEvents.add(event);
                }
            }
        }

        return nearbyEvents;
    }

    /**
     * @param category the category all returned habits must fall under
     * @return list of all habits falling under the specified category
     */
    public List<Habit> getHabitsInCategory(String category){
        List<Habit> list = new ArrayList<>();

        for (Habit habit : habits){
            if (habit.getType().equals(category)){
                list.add(habit);
            }
        }

        return list;
    }

    /**
     * @return the date this profile was made on
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the user's comment about their profile
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the user's comment about this profile
     * @param comment the user's new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the last time this user logged in
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * Set the last time this user logged in
     * @param lastLogin the date the user last logged in on
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Copy over the data from another profile into this one
     * @param other the profile to copy from
     */
    public void copyFrom(Profile other){
        name = other.name;
        habits = other.habits;
        following = other.following;
        followRequests = other.followRequests;
        powerPoints = other.powerPoints;
        habitPoints = other.habitPoints;
        creationDate = other.creationDate;
        comment = other.comment;
        image = other.image;
    }

    /**
     * Set the users this user is following
     * @param following the list of users this user is following (non-null)
     */
    public void setFollowing(List<Profile> following) {
        this.following = following;
    }

    /**
     * @return the profile's name
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Unfollow the specified user
     * @param followed the user to unfollow
     */
    public void unfollow(Profile followed){
        Iterator<Profile> profile = following.iterator();
        while (profile.hasNext()){
            if (profile.next().equals(followed)){
                profile.remove();
                return;
            }
        }
    }

    /**
     *
     * @return the user's profile picture encoded in base64
     */
    public String getImage() {
        return image;
    }

    /**
     * Set the user's profile picture
     * @param image the profile picture encoded in base64
     */
    public void setImage(String image) {
        this.image = image;
    }


    /**
     * Set the list of habits this profile has created
     * @param habits the list of habits this profile has created
     * @since Version 7
     */
    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

}
