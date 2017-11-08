/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 07 2017
 * This class represents a pending event to add a new habit event to a user's habit
 */

public class AddHabitEvent extends OfflineEvent {

    private HabitEvent toAdd;
    private String habitId;

    public AddHabitEvent(String habitId, HabitEvent event){
        toAdd = event;
        this.habitId = habitId;
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public void handleImpl(Profile profile) {
        // TODO: get profile from name, then add this event to the habit with matching name
        profile.setHabitPoints(profile.getHabitPoints() + 1);
        //profile.getHabitById(habitId).addEvent(toAdd)
    }
}
