package com.cmput301.cia;

import java.io.Serializable;

/**
 * Created by Adil on Oct 13 2017.
 */

public abstract class OfflineEvent implements Serializable {

    /**
     * What this event does when it is synchronized with the server
     */
    public abstract void handle();

}
