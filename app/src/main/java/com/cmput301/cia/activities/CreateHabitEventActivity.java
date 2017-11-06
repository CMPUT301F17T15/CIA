/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.ImageUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 5 2017
 *
 * This class represents the activity for creating a new habit event
 */

public class CreateHabitEventActivity extends AppCompatActivity {

    // The name of the intent data representing the owning habit's name
    public static final String ID_HABIT = "Habit";

    // Intent returned ID for the habit event
    public static final String RETURNED_HABIT = "HabitEvent";

    // All images can be at most this number of bytes
    public static final int MAX_IMAGE_SIZE = 65535;

    // Result code for selecting an image from gallery
    public static final int SELECT_IMAGE_CODE = 1;

    // the location the event occurred at
    private Location location;

    // the image attached to the event
    private Bitmap image;

    // The view displaying the image
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);

        location = null;
        image = null;

        Intent intent = getIntent();
        String habitName = intent.getStringExtra(ID_HABIT);
        ((TextView)findViewById(R.id.cheHabitNameText)).setText(habitName);
        imageView = (ImageView)findViewById(R.id.cheImageView);
        //habit = ElasticSearchUtilities.getObject(Habit.TYPE_ID, Habit.class, habitId);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void finishActivity(boolean cancelled){
        Intent intent = new Intent();

        String comment = ((EditText)findViewById(R.id.cheCommentEditText)).getText().toString();
        HabitEvent event = new HabitEvent(comment);

        // TODO: if incorrect format try removing the argument from constructor
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = format.parse(((EditText)findViewById(R.id.cheDateEditText)).getText().toString());
        } catch (ParseException e) {
            date = new Date();
        }
        event.setDate(date);
        event.setLocation(location);
        if (image != null){
            event.setBase64EncodedPhoto(ImageUtilities.imageToBase64(image));
        }

        intent.putExtra(RETURNED_HABIT, event);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * The following 2 functions are by Benjamin from
     * https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
     *
     * Permalink to specific comment:
     * https://stackoverflow.com/a/5309965
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);
                chosenImage = ImageUtilities.compressImageToMax(chosenImage, MAX_IMAGE_SIZE);

                if (chosenImage != null && chosenImage.getByteCount() <= MAX_IMAGE_SIZE) {
                    image = chosenImage;
                    updateImage();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the image view
     */
    private void updateImage(){
        if (image != null)
            imageView.setImageBitmap(image);
        else
            imageView.setImageAlpha(255);
    }

}
