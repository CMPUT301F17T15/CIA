/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

/**
 * @author Adil Malik
 * @version 4
 * Date: Nov 27 2017
 *
 * Represents a pending event that will be synchronized with the server when the user gains connectivity.
 * Follows the command pattern
 */

public abstract class OfflineEvent implements Serializable {

    // the event to do something with
    protected HabitEvent event;

    /**
     * Construct a new OfflineEvent command object
     * @param event the event to do something with
     */
    public OfflineEvent(HabitEvent event){
        this.event = event;
    }

    /**
     * What this event does when it is synchronized with the server
     * @param profile the user who this event is being handled for
     * @return whether the event was handled successfully or not
     */
    public abstract boolean handle(Profile profile);


}
