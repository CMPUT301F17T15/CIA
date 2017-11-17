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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.DatePickerUtilities;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.ImageUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tinghui, Adil Malik
 * @version 2
 *
 * created by Tinghui.
 * on 11/8/2017.
 */

public class HabitEventViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String RETURNED_EVENT = "Event", RETURNED_DELETED = "Deleted";

    // All images can be at most this number of bytes
    public static final int MAX_IMAGE_SIZE = 65535;

    // Result code for selecting an image from gallery
    public static final int SELECT_IMAGE_CODE = 1;

    private TextView habitEventDate;
    private TextView habitEventLocation;
    private ImageView habitEventPhoto;
    private EditText habitEventComment;

    private Button resetImageButton;

    private HabitEvent event;

    // the location the event occurred at
    private Location location;

    // the image attached to the event
    private Bitmap image;

    // The date this event occurred on
    private Date eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_view);
        /**
         * display habit event information.
         */
        Intent intent = getIntent();

        TextView habitEventName = (TextView) findViewById(R.id.vheHabitNameText);
        habitEventDate = (TextView) findViewById(R.id.vheDateDynamicText);
        habitEventLocation = (TextView) findViewById(R.id.vheLocationDynamicText);
        habitEventPhoto = (ImageView) findViewById(R.id.vhePhotoImage);
        habitEventComment = (EditText) findViewById(R.id.vheCommentDynamicText);
        resetImageButton = (Button)findViewById(R.id.vheResetImageButton);

        event = (HabitEvent) intent.getSerializableExtra("HabitEvent");

        // TODO: pass in the habit name instead
        Habit habit = ElasticSearchUtilities.getObject(Habit.TYPE_ID, Habit.class, event.getHabitId());
        if (habit != null)
            habitEventName.setText(habit.getTitle());

        location = event.getLocation();
        if (location != null)
            habitEventLocation.setText(DeviceUtilities.getLocationName(this, location));

        if (!event.getBase64EncodedPhoto().equals("")){
            image = ImageUtilities.base64ToImage(event.getBase64EncodedPhoto());
        } else
            image = null;

        updateImage();
        habitEventComment.setText(event.getComment());

        eventDate = event.getDate();
        setDateText();
        Toast.makeText(this, "Select the image to pick one to attach", Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked(View view){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    /**
     * the following three function basic on createhabitEventActivity.
     * Handles the save button being clicked
     * @param view
     */
    public void onSaveClicked(View view){
        finishActivity(false);
    }

    /**
     * Handles the delete button being clicked
     * @param view
     */
    public void onDeleteClicked(View view){
        finishActivity(true);
    }

    /**
     * End this activity after the user clicks on either "Edit" or "Delete"
     * Adds data to the intent object if the user selects "Save"
     * @param deleted whether the Delete button was clicked or not
     */
    private void finishActivity(boolean deleted){
        Intent intent = new Intent();
        String comment = habitEventComment.getText().toString();

        event.setComment(comment);

        event.setDate(eventDate);
        event.setLocation(location);
        if (image != null){
            event.setBase64EncodedPhoto(ImageUtilities.imageToBase64(image));
        } else
            event.setBase64EncodedPhoto("");

        intent.putExtra(RETURNED_EVENT, event);
        intent.putExtra(RETURNED_DELETED, deleted);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * The following 2 functions are based on Benjamin's answer from
     * https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
     *
     * Permalink to specific comment:
     * https://stackoverflow.com/a/5309965
     */
    public void onImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CreateHabitEventActivity.SELECT_IMAGE_CODE);
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
     * see: DatePickerUtilities
     * @param v: the layout that it's coming from
     */
    public void datePickerDialog(View v) {
        DatePickerUtilities datePickerFragment = new DatePickerUtilities();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /** for the date selected
     * @param datePicker : the widget object for selecting a date
     * @param year : the year chosen
     * @param month : the month chosen
     * @param day : the day chosen
     * see: DatePickerUtilities
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
        location = DeviceUtilities.getLocation(this);
        habitEventLocation.setText(DeviceUtilities.getLocationName(this, location));
    }

    /**
     * Update the view displaying the event's date in text form
     */
    private void setDateText(){
        habitEventDate.setText(DateUtilities.formatDate(eventDate));
    }

    /**
     * Update the image view after an image has been selected or removed
     */
    private void updateImage(){
        if (image != null) {
            habitEventPhoto.clearColorFilter();
            habitEventPhoto.setBackgroundColor(Color.rgb(255, 255, 255));
            habitEventPhoto.setImageBitmap(image);
            resetImageButton.setVisibility(View.VISIBLE);
        }
        else {
            habitEventPhoto.setColorFilter(Color.rgb(0, 0, 0));
            habitEventPhoto.setBackgroundColor(Color.rgb(0, 0, 0));
            resetImageButton.setVisibility(View.INVISIBLE);
        }
    }
}
