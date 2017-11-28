/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.util.List;

/**
 * @author Jessica Prieto
 * created November 27, 2017
 */

public class Follow {
    private List<String> followers;
    private List<String> following;

    public Follow(List<String> followers, List<String> following) {
        this.followers = followers;
        this.following = following;
    }

    public Follow() {
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}
