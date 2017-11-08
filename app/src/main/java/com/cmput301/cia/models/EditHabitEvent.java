/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 07 2017
 *
 * Represents an event that overwrites the old information of a habit event with new info
 */
public class EditHabitEvent extends OfflineEvent {

    private HabitEvent oldData;
    private HabitEvent newData;

    public EditHabitEvent(HabitEvent old, HabitEvent newEvent){
        oldData = old;
        newData = newEvent;
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public void handleImpl(Profile profile) {
        // TODO: copy newData into oldData

        // TODO: probably better to do this in a function oldData.copyFrom(newData)
        /*oldData.setBase64EncodedPhoto(newData.getBase64EncodedPhoto());
        oldData.setLocation(newData.getLocation());
        */
    }

}
