/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

/**
 * Created by Adil on Oct 14 2017.
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
     */
    @Override
    public void handle() {
        // TODO: copy newData into oldData
    }

}
