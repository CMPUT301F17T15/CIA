/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.TestProfile;
import com.cmput301.cia.activities.HomeTabbedActivity;
import com.cmput301.cia.activities.users.SearchUsersFragment;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.activities.users.UserProfileFragment;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 12 2017
 *
 * This class tests the UI for viewing a user's profile
 * NOTE: These tests require an internet connection
 */


public class ProfileIntentTests extends ActivityInstrumentationTestCase2<HomeTabbedActivity> {


    private Solo solo;

    public ProfileIntentTests() {
        super(com.cmput301.cia.activities.HomeTabbedActivity.class);
    }

    public void setUp() throws Exception{

        Profile profile = new TestProfile("xyz");
        profile.setFirstTimeUse(false);
        Intent intent = new Intent();
        intent.putExtra(HomeTabbedActivity.ID_PROFILE, profile);
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test to make sure the profile being viewed is the signed in user's profile
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testViewedProfile() throws NoSuchFieldException, IllegalAccessException {

        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_profile));
        solo.sleep(1000);

        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_profile));
        solo.sleep(1000);
        assertTrue("wrong fragment", getActivity().getFragmentForCurrentTab() instanceof UserProfileFragment);

        Field field = UserProfileFragment.class.getDeclaredField("displayed");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(getActivity().getFragmentForCurrentTab());

        Field field2 = UserProfileFragment.class.getDeclaredField("viewer");
        field2.setAccessible(true);

        Profile user = (Profile)field.get(getActivity().getFragmentForCurrentTab());

        assertTrue(viewed.equals(user));
    }

    /**
     * Test selecting the save button and making sure that the comment and image save as a result
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testSave() throws NoSuchFieldException, IllegalAccessException {

        String newComment = "this is a test comment @das";

        Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field2.setAccessible(true);
        Profile user = (Profile)field2.get(solo.getCurrentActivity());
        user.setComment("");
        assertFalse(user.getComment().equals(newComment));


        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_profile));
        solo.sleep(1000);
//        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        Field field = UserProfileFragment.class.getDeclaredField("displayed");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(getActivity().getFragmentForCurrentTab());

        viewed.setComment("");
        assertFalse(viewed.getComment().equals(newComment));

        solo.clearEditText((EditText)solo.getView(R.id.profileCommentDynamicText));
        solo.sleep(500);
        solo.enterText((EditText)solo.getView(R.id.profileCommentDynamicText), newComment);
        solo.clickOnButton("Save");
        solo.sleep(4000);
        solo.assertCurrentActivity("wrong activity", HomeTabbedActivity.class);

        assertTrue(user.getComment().equals(newComment));
    }

    /**
     * test viewing other people's profiles, along with following + unfollowing
     */
    public void testOtherProfile() throws NoSuchFieldException, IllegalAccessException {
        solo.clickOnView(getActivity().getBottomBarTabFromId(R.id.tab_search));
        solo.sleep(2500);
        assertTrue(getActivity().getFragmentForCurrentTab() instanceof SearchUsersFragment);

        solo.enterText(0, "vfutest13");
        solo.sleep(1000);
        solo.clickOnButton("Search");
        solo.sleep(2500);

        ListAdapter adapter = ((ListView) solo.getView(R.id.searchUsersList)).getAdapter();
        if (adapter.getCount() > 0) {
            solo.clickInList(1, 0);
            solo.sleep(2000);
            assertTrue(getActivity().getFragmentForCurrentTab() instanceof SearchUsersFragment);

            // the profile being displayed
            Field field = solo.getCurrentActivity().getClass().getDeclaredField("displayed");
            field.setAccessible(true);
            Profile viewed = (Profile)field.get(solo.getCurrentActivity());

            // the profile of the signed in user
            Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("viewer");
            field2.setAccessible(true);
            Profile viewer = (Profile)field2.get(solo.getCurrentActivity());

            assertTrue(!viewer.isFollowing(viewed));
            assertTrue(!viewed.hasFollowRequest(viewer));

            solo.clickOnButton("Follow");
            solo.sleep(1500);
            assertTrue("database error", viewed.hasFollowRequest(viewer));
            solo.sleep(1500);

            viewed.acceptFollowRequest(viewer);
            solo.sleep(1500);
            assertFalse("database error", viewed.hasFollowRequest(viewer));
            assertTrue("database error", viewer.isFollowing(viewed));

            solo.goBack();
            solo.sleep(3000);
            assertTrue(getActivity().getFragmentForCurrentTab() instanceof SearchUsersFragment);

            solo.clickInList(1, 0);
            solo.sleep(2000);
            solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

            solo.clickOnButton("Unfollow");
            solo.sleep(1500);
            assertFalse("database error", viewed.hasFollowRequest(viewer));
            assertFalse("database error", viewer.isFollowing(viewed));
            solo.sleep(1500);

            // cancel pending request
            solo.clickInList(1, 0);
            solo.sleep(2000);
            assertTrue(getActivity().getFragmentForCurrentTab() instanceof SearchUsersFragment);

            solo.clickOnButton("Follow");
            solo.sleep(1500);
            assertTrue("database error", viewed.hasFollowRequest(viewer));
            solo.sleep(1500);

            solo.clickOnButton("Pending");
            solo.sleep(1500);
            assertTrue("database error", !viewed.hasFollowRequest(viewer));
            solo.sleep(1500);
        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}