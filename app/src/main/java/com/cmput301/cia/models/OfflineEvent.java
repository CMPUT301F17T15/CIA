/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 07 2017
 *
 * Represents a pending event that will be synchronized with the server when the user gains connectivity
 */

public abstract class OfflineEvent implements Serializable {

    /**
     * What this event does when it is synchronized with the server
     * @param profile the user who this event is being handled for
     * @return whether the event was handled successfully or not
     */
    public final boolean handle(Profile profile){
        handleImpl(profile);
        profile.save();
        // TODO: return true only if profile saved successfully
        // TODO: probably should change everything (including ElasticSearchable.save()) to boolean and return (handleImpl(profile) && profile.save())
        return true;
    }

    /**
     * Implementation for what even does when it synchronizes with server
     * @param profile the user who this event is being handled for
     * @return whether the event was handled successfully or not
     */
    public abstract void handleImpl(Profile profile);

}
