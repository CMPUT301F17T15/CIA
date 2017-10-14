package com.cmput301.cia;

import java.io.Serializable;

/**
 * Created by Adil on Oct 13 2017.
 */

public abstract class OfflineEvent implements Serializable {

    public abstract void handle();

}
