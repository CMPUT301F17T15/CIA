/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import com.cmput301.cia.models.OfflineEvent;
import com.cmput301.cia.models.Profile;

/**
 *
 * @author Adil Malik
 * @version 1
 * Date: Nov 24 2017
 *
 * A mock profile class used only for testing
 */
public class TestProfile extends Profile {

    public TestProfile(){
        super("xyz");
        setId("A");
    }

    public TestProfile(String id){
        super("xyz");
        setId(id);
    }

    public void save(){

    }

    public void load(){

    }

}
