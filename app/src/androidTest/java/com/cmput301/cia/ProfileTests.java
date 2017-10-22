/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.models.Request;

/**
 * Created by WTH on 2017-10-22.
 */

public class ProfileTests extends ActivityInstrumentationTestCase2 {
    public ProfileTests() {super(MainActivity.class); }

    public void testUser(){
        String username = "name";
        Profile user = new Profile(username);
        assertEquals(user.getName(), username);
    }

    public void testFollowUser(){
        Profile requestTest = new Profile("name");
        Request test = new Request("name");
        requestTest.getFollowing();
    }
    /**
     * share
     */
    public void testShareUser(){
        Profile requestTest = new Profile("name");
        Request test = new Request("name");
        requestTest.getFollowing();

    }

    /**
     * following
     */
    public void testAddRequest(){
        Profile profile = new Profile("name");
        Request request = new Request("Mike");
        profile.addFollowRequest(profile);
        assertTrue(profile.hasFollowRequest(profile));
    }

    public void testRemoveRequest(){
        Profile profile = new Profile("name");
        Request request = new Request("Mike");
        profile.addFollowRequest(profile);
        profile.removeFollowRequest(profile);
        assertFalse(profile.hasFollowRequest(profile));
    }

    public void testFollowing(){
        Profile profile = new Profile("name");
        Request request = new Request("Mike");
        profile.addFollowRequest(profile);
        profile.follow(profile);
        assertFalse(profile.hasFollowRequest(profile));
    }







}
