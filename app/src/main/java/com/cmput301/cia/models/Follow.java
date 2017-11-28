/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jessica Prieto
 * created November 27, 2017
 */

public class Follow extends ElasticSearchable {
    public static final String TYPE_ID = "follow";

    private String userId;
    private String followerId; // the user to follow
    private String followeeId; // the user being followed
    private Status currentStatus;

    public enum Status {PENDING, ACCEPTED};

    public static void removeFollowRequest(String follower, String followee) {
        Follow toRemove = getFollow(follower, followee, Status.PENDING);
        toRemove.delete();
    }

    public static Follow getFollow(String followerId, String followeeId, Status currentStatus) {
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class);
        for (Follow currentFollow : follows) {
            if ((currentFollow.followerId.equals(followerId)) &&
                    (currentFollow.followeeId.equals(followeeId)) &&
                    (currentFollow.currentStatus == currentStatus)) {
                return currentFollow;
            }
        }
        return null;
    }

    public Follow(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.currentStatus = Status.PENDING;
    }

    public static Follow follow(String followerId, String followeeId) {
        Follow newFollow = new Follow(followerId, followeeId);
        newFollow.save();
        return newFollow;
    }

    public static List<String> getFollowing(String followerId) {
        List<String> following = new ArrayList<String>();
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class);
        
        for (Follow currentFollow : follows) {
            if (followerId.equals(currentFollow.getFollowerId()) && (currentFollow.currentStatus == Status.ACCEPTED)) {
                following.add(currentFollow.followeeId);
            }
        }
        return following;
    }

    public static List<String> getPendingFollows(String followeeId) {
        List<String> pendingFollowIds = new ArrayList<String>();
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class);

        for (Follow currentFollow : follows) {
            if (currentFollow.followeeId.equals(followeeId) && (currentFollow.currentStatus == Status.PENDING)) {
                pendingFollowIds.add(currentFollow.followerId);
            }
        }
        return pendingFollowIds;
    }

    public static boolean hasFollowRequest(String followeeId) {
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, Arrays.asList(followeeId));
        for (Follow currentFollow : follows) {
            if (followeeId.equals(currentFollow.getFolloweeId()) && currentFollow.currentStatus == Status.PENDING) {
                return true;
            }
        }
        return false;
    }

    public void acceptFollowRequest() {
        currentStatus = Status.ACCEPTED;
    }


    public void copyFrom(Follow follow){
        userId = follow.userId;
        followerId = follow.followerId;
        followeeId = follow.followeeId;
    }

    public void setFollowerId(String id) {
        followerId = id;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFolloweeId(String id) {
        followeeId = id;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public boolean save() {
        return ElasticSearchUtilities.save(this);
    }

    @Override
    public void load() {
        Follow found = ElasticSearchUtilities.getObject(getTypeId(), Follow.class, getId()).first;
        if (found != null){
            copyFrom(found);
        }
    }

    @Override
    public void delete() {
        ElasticSearchUtilities.delete(this);
    }
}
