/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Oct 13 2017
 *
 * Represents a pending event that will be synchronized with the server when the user gains connectivity
 */

public abstract class OfflineEvent implements Serializable {

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    public abstract boolean handle();

}
