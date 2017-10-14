/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 */

public class Profile {

    // The file where offline events will be stored
    private static final String OFFLINE_EVENTS_FILE = "events.sav";

    String name;
    List<Habit> habits;

    // list of users this user is following (unique)
    List<Profile> following;

    // users that have requested to follow this user (unique)
    List<Profile> followRequests;

    // Events that will be synchronized when the user signs in on a valid connection
    List<OfflineEvent> pendingEvents;

    /**
     * Construct a new user profile object
     * @param name the name of the user (not null)
     */
    public Profile(String name) {
        this.name = name;
        habits = new ArrayList<>();
        following = new ArrayList<>();
        followRequests = new ArrayList<>();
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
     * Save this profile to the database
     */
    public void save(){

    }

}
