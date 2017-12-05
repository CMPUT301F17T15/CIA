/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.templates.LocationRequestingActivity;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.fragments.DatePickerFragment;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.DialogUtils;
import com.cmput301.cia.utilities.LocationUtilities;
import com.cmput301.cia.utilities.ImageUtilities;
import com.cmput301.cia.views.ClickableEditItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Adil Malik
 * @version 4
 * Date: Nov 23 2017
 *
 * This class represents the activity for creating a new habit event
 */

public class CreateHabitEventActivity extends LocationRequestingActivity implements DatePickerDialog.OnDateSetListener {

    // The name of the intent data representing the owning habit's name, id, and index in the display list
    public static final String ID_HABIT_NAME = "Habit", ID_HABIT_HASH = "HabitID";//, ID_HABIT_INDEX = "Index";

    // Intent returned ID for the habit event
    public static final String RETURNED_HABIT = "HabitEvent";

    // All images can be at most this number of bytes
    public static final int MAX_IMAGE_SIZE = 65535;

    // Result code for selecting an image from gallery
    public static final int SELECT_IMAGE_CODE = 1;

    // The view displaying the image
    private FrameLayout imageViewFrame;
    private ImageView imageView;
    private ImageView uploadImage;

    // The view displaying the event date in text format
    private ClickableEditItem date;

    // The view displaying the location's name
    private ClickableEditItem locationItem;

    private ClickableEditItem comment;

    // the location the event occurred at
    private Location location;

    // the image attached to the event
    private Bitmap image;

    // The unique ID of the habit this event is being created for
    private String habitId;

    // The date this event occurred on
    private Date eventDate;

    private boolean isCommentChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);

        location = null;
        image = null;
        eventDate = new Date();

        Intent intent = getIntent();
        String habitName = intent.getStringExtra(ID_HABIT_NAME);
        habitId = intent.getStringExtra(ID_HABIT_HASH);

        this.setTitle(habitName + " Completion");

        imageView = (ImageView) findViewById(R.id.profileImageView);
        uploadImage = (ImageView) findViewById(R.id.uploadImage);
        imageViewFrame = (FrameLayout)findViewById(R.id.cheImageView);
        date = (ClickableEditItem) findViewById(R.id.editableDate);
        locationItem = (ClickableEditItem) findViewById(R.id.editableLocation);
        comment = (ClickableEditItem) findViewById(R.id.editableComment);

        //set on click listener to edit date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog(view);
            }
        });

        setDateText();

        // set on click listener to fetch location
        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAttachLocationClicked(view);
            }
        });

        // set on click listener to add comment
        isCommentChanged = false;
        comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String title = "Add comment";
                String hint = "Add comment to habit event";

                DialogUtils.createEditDialog(CreateHabitEventActivity.this, title, hint, new DialogUtils.OnOkClickedListener() {
                    @Override
                    public void onOkClicked(String editString) {
                        isCommentChanged = true;
                        comment.setItemDynamicText(editString);
                    }
                });
            }
        });


        if (isFinishing())
            return;
        Toast.makeText(this, "Select the image to pick one to attach", Toast.LENGTH_LONG).show();

        // handle the save button being clicked
        findViewById(R.id.cheSaveButton).setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                finishActivity(false);
            }
        });

        // handle the image being clicked
        imageViewFrame.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CreateHabitEventActivity.SELECT_IMAGE_CODE);
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

    /**
     * Handle the back button being pressed
     */
    @Override
    public void onBackPressed() {
        finishActivity(true);
    }

    /**
     * Handle the results of the image selection activity
     * @param requestCode id of the finished activity
     * @param resultCode code representing whether that activity was successful or not
     * @param data the data returned from that activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
         * based on Benjamin's answer from
         * https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
         *
         * Permalink to specific comment:
         * https://stackoverflow.com/a/5309965
         */

        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
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
     * Creates a dialog so that the user can choose a date instead of typing
     * see: DatePickerFragment
     * @param v: the layout that it's coming from
     */
    public void datePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /** for the date selected
     * @param datePicker : the widget object for selecting a date
     * @param year : the year chosen
     * @param month : the month chosen
     * @param day : the day chosen
     * see: DatePickerFragment
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Date date = calendar.getTime();
        Date currentDate = new Date();

        // Prevent the event's date from being a date in the future
        if (currentDate.before(date))
            date = currentDate;

        eventDate = date;
        setDateText();
    }

    /**
     * Reset the image view to nothing
     * @param view
     */
    public void onResetImageClicked(View view){
        image = null;
        updateImage();
    }

    /**
     * When the attach location button is clicked
     * @param view
     */
    public void onAttachLocationClicked(View view){
        requestLocationPermissions();
    }

    /**
     * Update the view displaying the event's date in text form
     */
    private void setDateText() {
        date.setItemDynamicText(DateUtilities.formatDate(eventDate));
    }

    /**
     * Update the image view after an image has been selected or removed
     */
    private void updateImage(){
        if (image != null) {
            uploadImage.setVisibility(View.GONE);
            imageView.clearColorFilter();
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
            imageView.setImageBitmap(image);
        }
        else {
            imageView.setColorFilter(Color.rgb(0, 0, 0));
            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
        }
    }

    /**
     * End this activity after the user clicks on either "Save" or "Cancel"
     * Adds data to the intent object if the user selects "Save"
     * @param cancelled whether the cancel button was clicked or not
     */
    private void finishActivity(boolean cancelled) {

        if (isFinishing())
            return;

        Intent intent = new Intent();
        if (cancelled) {
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            return;
        }

        // check if comment is added or not and set it properly on the new habitevent
        String commentString;
        if (isCommentChanged)
            commentString = comment.getDynamicText();
        else
            commentString = "";

        HabitEvent event = new HabitEvent(commentString);

        event.setDate(eventDate);
        event.setLocation(location);
        if (image != null){
            event.setBase64EncodedPhoto(ImageUtilities.imageToBase64(image));
        }

        intent.putExtra(RETURNED_HABIT, event);
        intent.putExtra(ID_HABIT_HASH, habitId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Handle the results of the request location permissions
     */
    @Override
    protected void handleLocationGranted() {
        location = LocationUtilities.getLocation(this);
        locationItem.setItemDynamicText(LocationUtilities.getLocationName(this, location));
    }

}
