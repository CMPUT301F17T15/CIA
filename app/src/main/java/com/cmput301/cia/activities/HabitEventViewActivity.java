/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.HabitEvent;

/**
 * created by Tinghui.
 * on 11/8/2017.
 */

public class HabitEventViewActivity extends AppCompatActivity {

    public static final String RETURNED_EVENT = "Event", RETURNED_DELETED = "Deleted";

    private TextView habitEventName;
    private TextView habitEventDate;
    private TextView habitEventLocation;
    private TextView habitEventPhoto;
    private EditText habitEventComment;

    private HabitEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_view);
        /**
         * display habit event information.
         */
        Intent intent = getIntent();

        habitEventName = (TextView) findViewById(R.id.EventName);
        habitEventDate = (TextView) findViewById(R.id.EventDate);
        habitEventLocation = (TextView) findViewById(R.id.EventLocation);
        habitEventPhoto = (TextView) findViewById(R.id.EventPhoto);
        habitEventComment = (EditText) findViewById(R.id.EditComment);

        event = (HabitEvent) getIntent().getSerializableExtra("HabitEvent");

        habitEventName.setText(event.getTittle());
        habitEventDate.setText(event.getDate().toString());

        if (event.getLocation() != null)
            habitEventLocation.setText(event.getLocation().toString());
        habitEventPhoto.setText(event.getBase64EncodedPhoto());
        habitEventComment.setText(event.getComment());

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
        String comment = ((EditText)findViewById(R.id.EditComment)).getText().toString();


        intent.putExtra(RETURNED_EVENT, event);
        intent.putExtra(RETURNED_DELETED, deleted);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
