/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
//<<<<<<< HEAD
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//>>>>>>> origin/master

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.models.Profile;

/**
 * @author Jessica Prieto
 * @version 4
 * Date: Nov 30 2017
 *
 * This activity displays the information about a user's profile
 */


public class UserProfileActivity extends AppCompatActivity {

    public static final String PROFILE_ID = "Profile", USER_ID = "User";
    public static final String RESULT_PROFILE_ID = "Profile";

    // the profile being displayed
    private Profile displayed;
    // the currently signed in user
    private Profile viewer;

//<<<<<<< HEAD
//=======
//    private Button followButton;
//    private Button unfollowButton;
//    private Button messageButton;
//
//    // the user's comment
//    private EditText commentText;
//
//    // the user's photo
//    private ImageView imageView;
//
//    // the image attached to the viewed user
//    private Bitmap image;
//
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//        super.onBackPressed();
//    }
//
//>>>>>>> origin/master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        displayed = (Profile) intent.getSerializableExtra(PROFILE_ID);
        viewer = (Profile) intent.getSerializableExtra(USER_ID);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.profileFragmentContainer, UserProfileFragment.create(displayed, viewer));
        ft.commit();

//=======
//        commentText = (EditText)findViewById(R.id.profileCommentDynamicText);
//        followButton = (Button)findViewById(R.id.profileFollowButton);
//        unfollowButton = (Button)findViewById(R.id.profileUnfollowButton);
//        messageButton = (Button) findViewById(R.id.MessageButton);
//
//        Button saveButton = (Button)findViewById(R.id.profileSaveButton);
//        imageView = (ImageView)findViewById(R.id.profileImageView);
//
//        // if viewer is viewing their own displayed
//        if (viewer.equals(displayed)){
//            followButton.setVisibility(View.INVISIBLE);
//            unfollowButton.setVisibility(View.INVISIBLE);
//            messageButton.setVisibility(View.INVISIBLE);
//        } else {
//            saveButton.setVisibility(View.INVISIBLE);
//            commentText.setEnabled(false);
//            imageView.setClickable(false);
//
//            if (viewer.isFollowing(displayed))
//                followButton.setVisibility(View.INVISIBLE);
//            else
//                unfollowButton.setVisibility(View.INVISIBLE);
//        }
//
//        commentText.setText(displayed.getComment());
//        ((TextView)findViewById(R.id.profileNameText)).setText(displayed.getName());
//        ((TextView)findViewById(R.id.profileDateDynamicText)).setText(DateUtilities.formatDate(displayed.getCreationDate()));
//
//        saveButton.setOnClickListener(new TimedClickListener() {
//            @Override
//            public void handleClick() {
//
//                Intent intent = new Intent();
//
//                // return the viewer
//                if (!viewer.equals(displayed))
//                    intent.putExtra(RESULT_PROFILE_ID, viewer);
//                else {
//                    // modify and return the viewer's displayed
//                    displayed.setComment(commentText.getText().toString());
//                    if (image != null)
//                        displayed.setImage(ImageUtilities.imageToBase64(image));
//
//                    intent.putExtra(RESULT_PROFILE_ID, displayed);
//                }
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//
//        if (displayed.hasFollowRequest(viewer)) {
//            followButton.setText(FOLLOW_BUTTON_MESSAGE_PENDING);
//        }
//
//        followButton.setOnClickListener(new TimedClickListener() {
//            @Override
//            public void handleClick() {
//                // send follow request if one is not sent
//                if (!displayed.hasFollowRequest(viewer)) {
//                    displayed.addFollowRequest(viewer);
//                    followButton.setText(FOLLOW_BUTTON_MESSAGE_PENDING);
//                    /*if (displayed.hasFollowRequest(viewer))
//                    else
//                        Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();*/
//                } else {
//                    // since a request has already been sent, remove it
//
//                    displayed.removeFollowRequest(viewer);
//                    followButton.setText(FOLLOW_BUTTON_MESSAGE_FOLLOW);
//                    /*if (!displayed.hasFollowRequest(viewer))
//                        followButton.setText(FOLLOW_BUTTON_MESSAGE_FOLLOW);
//                    else
//                        Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();*/
//                }
//            }
//        });
//
//        unfollowButton.setOnClickListener(new TimedClickListener() {
//            @Override
//            public void handleClick() {
//
//                viewer.unfollow(displayed);
//                /*if (viewer.isFollowing(displayed))
//                    Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();
//                else {
//                    unfollowButton.setVisibility(View.INVISIBLE);
//                    followButton.setVisibility(View.VISIBLE);
//                }*/
//                unfollowButton.setVisibility(View.INVISIBLE);
//                followButton.setVisibility(View.VISIBLE);
//            }
//        });
//
//        messageButton.setOnClickListener(new TimedClickListener() {
//            @Override
//            public void handleClick() {
//
//            }
//
//            });
//
//        imageView.setOnClickListener(new TimedClickListener() {
//            @Override
//            public void handleClick() {
//
//                // only the viewer can change their displayed's image
//                if (!displayed.equals(viewer))
//                    return;
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, SELECT_IMAGE_CODE);
//            }
//        });
//
//        if (displayed.getImage() == null || displayed.getImage().equals(""))
//            image = null;
//        else
//            image = ImageUtilities.base64ToImage(displayed.getImage());
//        updateImage();
//    }
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
