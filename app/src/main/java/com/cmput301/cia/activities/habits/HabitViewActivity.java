/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.views.ClickableEditItem;

import java.util.ArrayList;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * @author Shipin Guan
 * @version 2
 * Date: Nov 7, 2017
 *
 * This activity allows the user to view the details about one of their habits
 */

public class HabitViewActivity extends AppCompatActivity {

    private EditText habitName;
    private EditText habitReason;
    private Spinner habitTypeSpinner;

    private ClickableEditItem dateItem;
    private String StartDate;

    private MaterialDayPicker dayPicker;

    private static final String[] days = {"Sunday\n", "Monday\n", "Tuesday\n", "Wednesday\n", "Thursday\n", "Friday\n", "Saturday\n"};

    private Habit habit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);

        Button toStatisticButton = (Button) findViewById(R.id.toStatistic);
        Button deleteButton = (Button) findViewById(R.id.DeleteHabitButton);
        Button editButton = (Button) findViewById(R.id.SaveHabitButton);

        //Display habit detail
        habitName = (EditText) findViewById(R.id.habitName);
        habitReason = (EditText) findViewById(R.id.habitReason);
        habitTypeSpinner = (Spinner) findViewById(R.id.habitTypeSpinner);

        dateItem = (ClickableEditItem) findViewById(R.id.habitDetailDate);
        StartDate = dateItem.getDynamicText();

        dayPicker = (MaterialDayPicker) findViewById(R.id.day_picker);

        habit = (Habit) getIntent().getSerializableExtra("Habit");
        final ArrayList<String> categories = getIntent().getStringArrayListExtra("Categories");

        // for the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitTypeSpinner.setAdapter(spinnerAdapter);

        refreshPage();

        toStatisticButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent intent = new Intent(HabitViewActivity.this, SingleStatisticViewActivity.class);
                intent.putExtra(SingleStatisticViewActivity.ID_HABIT, habit);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {

            /**
             * Reference: https://developer.android.com/guide/topics/ui/dialogs.html
             */
            // Ask the user for confirmation before a habit is deleted
            AlertDialog.Builder dialog = new AlertDialog.Builder(HabitViewActivity.this);
            dialog.setTitle("Are you sure you want to delete this habit?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                if (isFinishing())
                    return;

                Intent returnIntent = new Intent();
                returnIntent.putExtra("Deleted", true);
                returnIntent.putExtra("HabitID", habit.getId());
                setResult(RESULT_OK, returnIntent);
                finish();
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            if (!isFinishing())
                dialog.show();
            }
        });

        editButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                Intent editIntent = new Intent(HabitViewActivity.this, EditHabitActivity.class);
                editIntent.putExtra("Habit", habit);
                editIntent.putExtra("Categories", categories);
                startActivityForResult(editIntent, 1);
            }
        });
    }

    /**
     * Handle the back button being pressed
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("Habit", habit);
        intent.putExtra("Deleted", false);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Handle the results of an activity that has finished
     * @param requestCode the activity's identifying code
     * @param resultCode the result status of the finished activity
     * @param data the activity's returned intent information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                habit = (Habit) data.getSerializableExtra("Habit");
                refreshPage();
            }
        }
    }

    private void refreshPage() {
//        habitTypeSpinner.setText(habit.getType());
        // TODO: set spinner to type
        selectSpinnerItemByValue(habitTypeSpinner, habit.getType());

        habitName.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        dateItem.setItemDynamicText(DateUtilities.formatDate(habit.getStartDate()));

        String temp = "";
        for (int i : habit.getDaysOfWeek()) {
            dayPicker.selectDay(MaterialDayPicker.Weekday.values()[i-1]);
        }
    }

    /**
     *
     * @param spnr
     * @param value
     *
     * solution based on an answer from stack overflow
     * resource: https://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
     *
     */

    public static void selectSpinnerItemByValue(Spinner spnr, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnr.getAdapter();
        String currentItem;

        for (int i = 0; i < adapter.getCount(); i++) {
            currentItem = adapter.getItem(i);
            if(currentItem.equals(value)) {
                spnr.setSelection(i);
                return;
            }
        }
    }

}
