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
 * @version 1
 * Date: November 27, 2017
 *
 * This class represents an object for the following activity:
 * Each follow activity is stored as a Follow class (user following another user &
 * pending requests)
 */

public class Follow extends ElasticSearchable {
    public static final String TYPE_ID = "follow";

    //the user doing the following
    private String followerId;

    //the user being followed
    private String followeeId;

    //follow status: accepted or pending
    private Status currentStatus;

    //Enum to differentiate the status
    public enum Status {PENDING, ACCEPTED};

    /**
     * Constructs a new Follow object
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     */
    public Follow(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.currentStatus = Status.PENDING;
    }

    /**
     * removes the follow request by deleting the instance of the follow objects
     * @param follower the user doing the following
     * @param followee the user being followed
     */
    public static void removeFollowRequest(String follower, String followee) {
        Follow toRemove = getFollow(follower, followee, Status.PENDING);
        toRemove.delete();
        toRemove.save();
    }

    /**
     * Returns the follow object given the parameters
     *
     * @param followerId the user doing the following
     * @param followeeId the user being followed
     * @param currentStatus the status of the follow action : Pending of Approved
     * @return the follow object
     */
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

    /**
     * User follows another user by creating a new follow instance
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     * @return the new instance of the follow object after creation
     */
    public static Follow follow(String followerId, String followeeId) {
        Follow newFollow = new Follow(followerId, followeeId);
        newFollow.save();
        return newFollow;
    }

    /**
     * Returns a list of the users that the current user is following
     *
     * @param followerId the user doing the following
     * @return a list of strings containing the ID's of the users that the
     *      user is following
     */
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

    /**
     * Gets a list of pending follow requests
     *
     * @param followeeId the current user with the follow requests
     * @return a list of strings containing the users wanting to follow the current user
     */
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

    /**
     * checks to see if the user has any pending follow requests
     *
     * @param followeeId the current user to check pending follow requests
     *
     * @return a boolean determining if the user has any follow requests
     */
    public static boolean hasFollowRequest(String followeeId) {
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, Arrays.asList(followeeId));
        for (Follow currentFollow : follows) {
            if (followeeId.equals(currentFollow.getFolloweeId()) && currentFollow.currentStatus == Status.PENDING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accepts the follow request by changing the status from pending to accepted
     */
    public void acceptFollowRequest() {
        currentStatus = Status.ACCEPTED;
        save();
    }

    /** helper function for the load method.
     *
     * @param follow the follow object to load
     */
    public void copyFrom(Follow follow){
        followerId = follow.followerId;
        followeeId = follow.followeeId;
    }

    /** returns the follower id (the user doing the "following" action)
     *
     * @return string of the follower ID
     */
    public String getFollowerId() {
        return followerId;
    }

    /**
     * sets the follower ID (the user doing the "following" action)
     * @param id the ID of the user to set in the object
     */
    public void setFolloweeId(String id) {
        followeeId = id;
    }

    /** retrieves the Id of the followee (the person receiving the "following" action)
     *
     * @return string of the ID of the followee
     */
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
