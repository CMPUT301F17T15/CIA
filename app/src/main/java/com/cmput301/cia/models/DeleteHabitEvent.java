/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 18 2017
 * This class represents a pending event to delete a user's habit event
 */

public class DeleteHabitEvent extends OfflineEvent {

    private HabitEvent toDelete;
    private String profileName;
    private String habitName;

    public DeleteHabitEvent(String userName, String habitName, HabitEvent event){
        toDelete = event;
        profileName = userName;
        this.habitName = habitName;
    }

    /**
     * What this event does when it is synchronized with the server
     */
    @Override
    public void handle() {
        // TODO: get profile from name, then delete this event from the habit with matching name
    }
}
