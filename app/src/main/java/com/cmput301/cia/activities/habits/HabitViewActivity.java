/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DateUtilities;

import java.util.ArrayList;

/**
 * @author Shipin Guan
 * @version 2
 * Date: Nov 7, 2017
 *
 * This activity allows the user to view the details about one of their habits
 */

public class HabitViewActivity extends AppCompatActivity {

    private TextView habitName;
    private TextView habitType;
    private TextView habitReason;
    private TextView habitStartDate;
    private TextView habitFrequency;

    private static final String[] days = {"Sunday\n", "Monday\n", "Tuesday\n", "Wednesday\n", "Thursday\n", "Friday\n", "Saturday\n"};

    private Habit habit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);

        Button deleteButton = (Button) findViewById(R.id.DeleteHabitButton);
        Button editButton = (Button) findViewById(R.id.EditHabitButton);

        //Display habit detail
        habitName = (TextView) findViewById(R.id.EditHabitName);
        habitType = (TextView) findViewById(R.id.EditHabitType);
        habitReason = (TextView) findViewById(R.id.EditHabitReason);
        habitStartDate = (TextView) findViewById(R.id.StartingDate);
        habitFrequency = (TextView) findViewById(R.id.HabitFrequency);

        habit = (Habit) getIntent().getSerializableExtra("Habit");
        final ArrayList<String> categories = getIntent().getStringArrayListExtra("Categories");
        refreshPage();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Reference: https://developer.android.com/guide/topics/ui/dialogs.html
                 */
                // Ask the user for confirmation before a habit is deleted
                AlertDialog.Builder dialog = new AlertDialog.Builder(HabitViewActivity.this);
                dialog.setTitle("Are you sure you want to delete this habit?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                dialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
        habitType.setText(habit.getType());
        habitName.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitStartDate.setText(DateUtilities.formatDate(habit.getStartDate()));
        String temp = "";
        for (int i : habit.getDaysOfWeek()) {
            temp = temp + days[i - 1];
        }
        habitFrequency.setText(temp);
    }

    //Crate the menu object
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
