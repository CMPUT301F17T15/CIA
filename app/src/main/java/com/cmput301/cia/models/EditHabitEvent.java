/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * @author Adil Malik
 * @version 4
 * Date: Nov 27 2017
 *
 * Represents an event that overwrites the old information of a habit event with new info
 */

public class EditHabitEvent extends OfflineEvent {

    /**
     * Construct a new command object that will edit the specified event when executed
     * @param event the new data of the event
     */
    public EditHabitEvent(HabitEvent event){
        super(event);
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public boolean handle(Profile profile) {
        Habit habit = profile.getHabitById(event.getHabitId());
        if (habit != null)
            habit.editEvent(event);
        return profile.save();
    }

}
