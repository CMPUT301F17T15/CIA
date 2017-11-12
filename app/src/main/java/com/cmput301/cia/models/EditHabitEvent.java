/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * Version 3
 * Author: Adil Malik
 * Date: Nov 11 2017
 *
 * Represents an event that overwrites the old information of a habit event with new info
 */

public class EditHabitEvent extends OfflineEvent {

    private HabitEvent newData;
    private String habitId;

    public EditHabitEvent(String habitId, HabitEvent newEvent){
        newData = newEvent;
        this.habitId = habitId;
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public void handleImpl(Profile profile) {
        Habit habit = profile.getHabitById(habitId);
        for (HabitEvent event : habit.getEvents()){
            if (event.equals(newData)){
                event.copyFrom(newData);
            }
        }
    }

}
