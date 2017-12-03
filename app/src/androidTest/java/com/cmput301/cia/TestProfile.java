/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import com.cmput301.cia.models.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adil Malik
 * @version 1
 * Date: Nov 24 2017
 *
 * A mock profile class used only for testing
 * This is created to prevent saving/loading from the database
 */
public class TestProfile extends Profile {

    public TestProfile(){
        super("xyz");
        setId("A");
        setFirstTimeUse(false);
    }

    public TestProfile(String id){
        super("xyz");
        setId(id);
        setFirstTimeUse(false);
    }

    /**
     * Override to prevent saving
     */
    public boolean save(){
        return true;
    }

    /**
     * Override to prevent loading
     */
    public void load(){

    }

}
