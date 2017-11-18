/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

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
        Button finishButton = (Button)findViewById(R.id.dhdReturnButton);

        //Create custom tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar_habit_detail);
        setSupportActionBar(toolbar);
        //Display habit detail
        habitName = (TextView) findViewById(R.id.EditHabitName);
        habitType = (TextView) findViewById(R.id.EditHabitType);
        habitReason = (TextView) findViewById(R.id.EditHabitReason);
        habitStartDate = (TextView) findViewById(R.id.StartingDate);
        habitFrequency = (TextView) findViewById(R.id.HabitFrequency);

        habit = (Habit) getIntent().getSerializableExtra("Habit");
        final ArrayList<String> categories = getIntent().getStringArrayListExtra("Categories");
        RefreshPage();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Deleted", true);
                returnIntent.putExtra("HabitID", habit.getId());
                setResult(RESULT_OK, returnIntent);
                finish();
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

        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.putExtra("Habit", habit);
                intent.putExtra("Deleted", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
                RefreshPage();
            }
        }
    }

    private void RefreshPage() {
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

    //Menu item onclick bridge to specific activity.
    //use startActivityForResult instead of startActivity for return value or refresh home page.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_button_My_Profile:
                Intent intent_My_Profile = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_My_Profile);
                return true;
            case R.id.menu_button_Add_New_Habit:
                Intent intent_new_Habit = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_new_Habit);
                return true;
            case R.id.menu_button_Statistic:
                Intent intent_Statistic = new Intent(this, StatisticActivity.class);
                startActivity(intent_Statistic);
                return true;
            case R.id.menu_button_Habit_History:
                Intent intent_Habit_History = new Intent(this, HistoryActivity.class);
                startActivity(intent_Habit_History);
                return true;
            case R.id.menu_button_My_Following:
                Intent intent_My_Following = new Intent(this, CreateHabitActivity.class);
                startActivity(intent_My_Following);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
