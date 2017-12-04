/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ImageUtilities;

import java.io.IOException;
import java.io.InputStream;

import static com.cmput301.cia.activities.events.CreateHabitEventActivity.MAX_IMAGE_SIZE;

/**
 * @author Adil Malik, Shipin Guan, Jessica Prieto
 * @version 4
 * Date: Nov 30 2017
 *
 * This activity displays the information about a user's profile
 */

public class UserProfileFragment extends Fragment {

    public static final String PROFILE_ID = "Profile", USER_ID = "User";
    public static final String RESULT_PROFILE_ID = "Profile";

    private static final String FOLLOW_BUTTON_MESSAGE_FOLLOW = "FOLLOW";
    private static final String FOLLOW_BUTTON_MESSAGE_PENDING = "PENDING";

    // Result code for selecting an image from gallery
    public static final int SELECT_IMAGE_CODE = 1;

    // the profile being displayed
    private Profile displayed;
    // the currently signed in user
    private Profile viewer;

    private Button followButton;
    private Button unfollowButton;
    private Button sendButton;

    // the displayed's comment
    private EditText commentText;
//    private EditText sendMessage;

    // the displayed's photo
    private ImageView imageView;
    private FrameLayout imageViewFrame;
    private ImageView uploadIcon;

    // the image attached to the viewed displayed
    private Bitmap image;

    public static Fragment create(Profile profile, Profile user) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROFILE_ID, profile);
        args.putSerializable(USER_ID, user);
        userProfileFragment.setArguments(args);
        return userProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        displayed = (Profile) getArguments().getSerializable(PROFILE_ID);
        viewer = (Profile) getArguments().getSerializable(USER_ID);

        getActivity().setTitle(displayed.getName());

        // initialize view member variables
//        TextView nameText = (TextView) view.findViewById(R.id.profileNameText);
//        sendMessage = (EditText) view.findViewById(R.id.editText);
//        sendMessage.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        sendMessage.setGravity(Gravity.TOP);
//        sendMessage.setSingleLine(false);
//        sendMessage.setHorizontallyScrolling(false);

        commentText = (EditText) view.findViewById(R.id.profileCommentDynamicText);
        followButton = (Button) view.findViewById(R.id.profileFollowButton);
        unfollowButton = (Button) view.findViewById(R.id.profileUnfollowButton);
//        sendButton = (Button) view.findViewById(R.id.profileSendButton);
        Button saveButton = (Button) view.findViewById(R.id.profileSaveButton);
        imageView = (ImageView) view.findViewById(R.id.profileImageView);
        imageViewFrame = (FrameLayout) view.findViewById(R.id.profileImageViewFrame);
        uploadIcon = (ImageView) view.findViewById(R.id.uploadImage);

        // if viewer is viewing their own displayed
        if (viewer.equals(displayed)){
            followButton.setVisibility(View.INVISIBLE);
            unfollowButton.setVisibility(View.INVISIBLE);
        } else {
            saveButton.setVisibility(View.INVISIBLE);
            commentText.setEnabled(false);
            imageView.setClickable(false);
            uploadIcon.setVisibility(View.GONE);

            if (viewer.isFollowing(displayed))
                followButton.setVisibility(View.INVISIBLE);
            else
                unfollowButton.setVisibility(View.INVISIBLE);
        }

        commentText.setText(displayed.getComment());
//        nameText.setText(displayed.getName());

        ((TextView) view.findViewById(R.id.profileDateDynamicText)).setText("Registered on: " + DateUtilities.formatDate(displayed.getCreationDate()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent();

            // return the viewer
            if (!viewer.equals(displayed))
                intent.putExtra(RESULT_PROFILE_ID, viewer);
            else {
                // modify and return the viewer's displayed
                displayed.setComment(commentText.getText().toString());
                if (image != null)
                    displayed.setImage(ImageUtilities.imageToBase64(image));

                displayed.save();

//                    intent.putExtra(RESULT_PROFILE_ID, displayed);
            }

            Toast.makeText(getContext(), "changes saved", Toast.LENGTH_SHORT).show();
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });

        if (displayed.hasFollowRequest(viewer)) {
            followButton.setText(FOLLOW_BUTTON_MESSAGE_PENDING);
        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // send follow request if one is not sent
                if (!displayed.hasFollowRequest(viewer)) {
                    displayed.addFollowRequest(viewer);
                    followButton.setText(FOLLOW_BUTTON_MESSAGE_PENDING);
                    /*if (displayed.hasFollowRequest(viewer))
                    else
                        Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();*/
                } else {
                    // since a request has already been sent, remove it

                    displayed.removeFollowRequest(viewer);
                    followButton.setText(FOLLOW_BUTTON_MESSAGE_FOLLOW);
                    /*if (!displayed.hasFollowRequest(viewer))
                        followButton.setText(FOLLOW_BUTTON_MESSAGE_FOLLOW);
                    else
                        Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();*/
                }
            }
        });

        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewer.unfollow(displayed);
                /*if (viewer.isFollowing(displayed))
                    Toast.makeText(UserProfileActivity.this, "Could not connect to the database", Toast.LENGTH_SHORT).show();
                else {
                    unfollowButton.setVisibility(View.INVISIBLE);
                    followButton.setVisibility(View.VISIBLE);
                }*/
                unfollowButton.setVisibility(View.INVISIBLE);
                followButton.setVisibility(View.VISIBLE);
            }
        });

//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = sendMessage.getText().toString();
//                displayed.sendMessage(message);
//                displayed.save();
//            }
//        });


        imageViewFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // only the viewer can change their displayed's image
                if (!displayed.equals(viewer))
                    return;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_IMAGE_CODE);
            }
        });

        if (displayed.getImage() == null || displayed.getImage().equals(""))
            image = null;
        else
            image = ImageUtilities.base64ToImage(displayed.getImage());

        updateImage();
    }

    /**
     * Handle the results of the image selection activity
     * @param requestCode id of the finished activity
     * @param resultCode code representing whether that activity was successful or not
     * @param data the data returned from that activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {

                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);

                // attempt to resize the image if necessary
                chosenImage = ImageUtilities.compressImageToMax(chosenImage, MAX_IMAGE_SIZE);

                if (chosenImage == null) {
                    image = null;
                    updateImage();
                } else if (chosenImage.getByteCount() <= MAX_IMAGE_SIZE) {
                    image = chosenImage;
                    updateImage();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the image view after an image has been selected or removed
     */
    private void updateImage(){
        if (image != null) {
            uploadIcon.setVisibility(View.GONE);
            imageView.clearColorFilter();
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
            imageView.setImageBitmap(image);
        }
        else {
//            imageView.setColorFilter(Color.rgb(0, 0, 0));
//            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
        }
    }
}
