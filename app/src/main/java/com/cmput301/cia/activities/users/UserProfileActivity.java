/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

/**
 * Version 1
 * Authors: Adil Malik, Shipin Guan
 * Date: Nov 11 2017
 *
 * This activity displays the information about a user's profile
 */

// TODO: following/unfollowing, image, messages

public class UserProfileActivity extends AppCompatActivity {

    public static final String PROFILE_ID = "Profile", USER_ID = "User";
    public static final String RESULT_COMMENT_ID = "Comment", RESULT_IMAGE_ID = "Image";

    // the profile being displayed
    private Profile profile;
    // the currently signed in user
    private Profile user;

    private Button followButton;
    private Button unfollowButton;
    private Button saveButton;

    // the profile's name
    private TextView nameText;

    // the profile's comment
    private EditText commentText;

    // the profile's photo
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        String profileId = intent.getStringExtra(PROFILE_ID);
        String userId = intent.getStringExtra(USER_ID);

        // initialize the profiles
        profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, profileId);
        if (profileId.equals(userId))
            user = profile;
        else
            user = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, userId);

        // connection error
        if (profile == null || user == null){
            Toast.makeText(this, "Could not retrieve profile from the server.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // initialize view member variables
        nameText = (TextView)findViewById(R.id.profileNameText);
        commentText = (EditText)findViewById(R.id.profileCommentDynamicText);
        followButton = (Button)findViewById(R.id.profileFollowButton);
        unfollowButton = (Button)findViewById(R.id.profileUnfollowButton);
        saveButton = (Button)findViewById(R.id.profileSaveButton);
        image = (ImageView)findViewById(R.id.profileImageView);

        // if user is viewing their own profile
        if (user.equals(profile)){
            followButton.setVisibility(View.INVISIBLE);
            unfollowButton.setVisibility(View.INVISIBLE);
        } else {
            saveButton.setVisibility(View.INVISIBLE);
            commentText.setEnabled(false);
            image.setClickable(false);
        }

        commentText.setText(profile.getComment());
        nameText.setText(profile.getName());

        ((TextView)findViewById(R.id.profileDateDynamicText)).setText(DateUtilities.formatDate(profile.getCreationDate()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.setComment(commentText.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(RESULT_COMMENT_ID, profile.getComment());
                // TODO: image
                intent.putExtra(RESULT_IMAGE_ID, "");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.addFollowRequest(user);
            }
        });

        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                //user.unfollow(profile);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
