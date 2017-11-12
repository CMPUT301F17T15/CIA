/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.HabitEvent;

/**
 * created by Tinghui.
 * on 11/8/2017.
 */

public class HabitEventViewActivity extends AppCompatActivity {

    private TextView habitEventName;
    private TextView habitEventDate;
    private TextView habitEventLocation;
    private TextView habitEventPhoto;
    TextView habitEventComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_view);
        /**
         * display habit event information.
         */
        habitEventName = (TextView) findViewById(R.id.EventName);
        habitEventDate = (TextView) findViewById(R.id.EventDate);
        habitEventLocation = (TextView) findViewById(R.id.EventLocation);
        habitEventPhoto = (TextView) findViewById(R.id.EventPhoto);
        habitEventComment = (TextView) findViewById(R.id.EventComment);

        HabitEvent habitevent = (HabitEvent) getIntent().getSerializableExtra("HabitEvent");

        habitEventName.setText(habitevent.getTittle());
        habitEventDate.setText(habitevent.getDate().toString());
        habitEventLocation.setText(habitevent.getLocation().toString());
        habitEventPhoto.setText(habitevent.getBase64EncodedPhoto());
        habitEventComment.setText(habitevent.getComment());

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
        if (deleted) {

        }
        

        
    }
}
