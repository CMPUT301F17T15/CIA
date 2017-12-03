/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import android.util.Pair;

import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jessica Prieto
 * @version 1
 * Date: November 27, 2017
 *
 * This class represents an object for the following activity:
 * Each sendFollowRequest activity is stored as a Follow class (user following another user &
 * pending requests)
 */

public class Follow extends ElasticSearchable {

    public static final String TYPE_ID = "following";

    private static final String PENDING_STR = "0", ACCEPTED_STR = "1";

    //the user doing the following
    private String followerId;

    //the user being followed
    private String followeeId;

    // whether this object represents a pending (requested) or accepted sendFollowRequest
    // TODO: change to boolean if it doesn't break elasticsearch
    private String accepted;

    /**
     * Constructs a new Follow object
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     */
    public Follow(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        accepted = PENDING_STR;
    }

    /**
     * Constructs a new Follow object
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     * @param request whether this object represents a requested sendFollowRequest or an accepted one
     */
    public Follow(String followerId, String followeeId, boolean request) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        accepted = request ? PENDING_STR : ACCEPTED_STR;
    }

    /**
     * removes the sendFollowRequest request by deleting the instance of the sendFollowRequest objects
     * @param follower the user doing the following
     * @param followee the user being followed
     */
    public static void removeFollowRequest(String follower, String followee) {
        Map<String, String> map = new HashMap<>();
        map.put("followerId", follower);
        map.put("followeeId", followee);
        map.put("accepted", "0");
        ElasticSearchUtilities.delete(Follow.TYPE_ID, Follow.class, map);
    }

    /**
     * Send a follow request from one user to another
     * @param followerId the user doing the following
     * @param followeeId the user being followed
     * @return whether the follow request was successfully sent or not
     */
    public static boolean sendFollowRequest(String followerId, String followeeId) {
        Follow newFollow = new Follow(followerId, followeeId);
        return newFollow.save();
    }

    /**
     * Allow the follower to follow the followee
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     * @param requested whether this object represents a requested follow or an accepted one
     * @return the new instance of the sendFollowRequest object after creation
     */
    public static Follow follow(String followerId, String followeeId, boolean requested) {
        Follow newFollow = new Follow(followerId, followeeId, requested);
        newFollow.save();
        return newFollow;
    }

    /**
     * Remove the followee from the list of users the follower is following
     * @param followerId the user being followed
     * @param followeeId the user doing the following
     */
    public static void unfollow(String followerId, String followeeId) {
        Map<String, String> map = new HashMap<>();
        map.put("followerId", followerId);
        map.put("followeeId", followeeId);
        map.put("accepted", "1");
        ElasticSearchUtilities.delete(Follow.TYPE_ID, Follow.class, map);
    }

    /**
     * Returns a list of the users that the current user is following
     *
     * @param followerId the user doing the following
     * @return a list of strings containing the ID's of the users that the
     *      user is following
     */
    public static List<Profile> getFollowing(String followerId) {
        Map<String, String> map = new HashMap<>();
        map.put("followerId", followerId);
        map.put("accepted", "1");
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, map);

        List<Profile> following = new ArrayList<>();
        for (Follow follow : follows){
            Pair<Profile, Boolean> result = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, follow.followeeId);
            if (result.first != null)
                following.add(result.first);
        }

        return following;
    }

    /**
     * Gets a list of pending sendFollowRequest requests
     *
     * @param followeeId the current user with the sendFollowRequest requests
     * @return a list of strings containing the users wanting to sendFollowRequest the current user
     */
    public static List<Profile> getPendingFollows(String followeeId) {
        Map<String, String> map = new HashMap<>();
        map.put("followeeId", followeeId);
        map.put("accepted", "0");
        List<Follow> follows = ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, map);

        List<Profile> following = new ArrayList<>();
        for (Follow follow : follows){
            Pair<Profile, Boolean> result = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, follow.followerId);
            if (result.first != null)
                following.add(result.first);
        }

        return following;
    }

    /**
     * checks to see if the user has a pending follow request from another user
     * @param followeeId the id of the user to check if they are being followed or not
     * @param followerId the id of the user to check if they have sent a follow request to the other user
     * @return a boolean determining if the followee has a follow request from the specified follower
     */
    public static boolean hasFollowRequest(String followeeId, String followerId) {
        Map<String, String> map = new HashMap<>();
        map.put("followeeId", followeeId);
        map.put("followerId", followerId);
        map.put("accepted", "0");
        return ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, map).size() > 0;
    }

    /**
     * checks to see if a follower is following another user
     * @param followerId the id of the user to check if they are following the other user
     * @param followeeId the id of the user to check if they are being followed or not
     * @return a boolean determining if the followee has a follow request from the specified follower
     */
    public static boolean isFollowing(String followerId, String followeeId) {
        Map<String, String> map = new HashMap<>();
        map.put("followeeId", followeeId);
        map.put("followerId", followerId);
        map.put("accepted", "1");
        return ElasticSearchUtilities.getListOf(Follow.TYPE_ID, Follow.class, map).size() > 0;
    }

    /**
     * Accepts the sendFollowRequest request by changing the status from pending to accepted
     */
    public static void acceptFollowRequest(String followerId, String followeeId) {
        Map<String, String> map = new HashMap<>();
        map.put("followeeId", followeeId);
        map.put("followerId", followerId);
        map.put("accepted", "0");
        Pair<Follow, Boolean> result = ElasticSearchUtilities.getObject(Follow.TYPE_ID, Follow.class, map);
        if (result.first != null){
            result.first.accepted = ACCEPTED_STR;
            result.first.save();
        }
    }

    /** helper function for the load method.
     *
     * @param follow the sendFollowRequest object to load
     */
    public void copyFrom(Follow follow){
        followerId = follow.followerId;
        followeeId = follow.followeeId;
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
