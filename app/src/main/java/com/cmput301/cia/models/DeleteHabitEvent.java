/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 07 2017
 * This class represents a pending event to delete a user's habit event
 */

public class DeleteHabitEvent extends OfflineEvent {

    private HabitEvent toDelete;
    private String habitId;

    public DeleteHabitEvent(String habitId, HabitEvent event){
        toDelete = event;
        this.habitId = habitId;
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public void handleImpl(Profile profile) {
        // TODO: test
        profile.getHabitById(habitId).removeHabitEvent(toDelete);
    }
}
