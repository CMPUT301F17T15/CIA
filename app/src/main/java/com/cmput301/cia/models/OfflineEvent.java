/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

/**
 * @author Adil Malik
 * @version 3
 * Date: Nov 12 2017
 *
 * Represents a pending event that will be synchronized with the server when the user gains connectivity.
 * Follows the command pattern, as well as the template method pattern
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
    public final boolean handle(Profile profile){
        handleImpl(profile);
        profile.save();
        // TODO: probably should change ElasticSearchable.save() to boolean and return profile.save()
        return true;
    }

    /**
     * Implementation for what even does when it synchronizes with server
     * @param profile the user who this event is being handled for
     * @return whether the event was handled successfully or not
     */
    public abstract void handleImpl(Profile profile);

    /**
     * @return the habit ID of the habit containing the habit event
     */
    public String getHabitId(){
        return event.getHabitId();
    }

}
