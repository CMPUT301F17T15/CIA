/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 * Created by: Jessica Prieto
 * Date: 04-11-2017
 *
 * Version 1.0
 */

package com.cmput301.cia.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.CreateHabitController;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DatePickerUtilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** activity for creating a new habit by letting the user input:
 *      - habit name
 *      - reason
 *      - start date
 *      - frequency (days of the week to do the habit)
 */
public class CreateHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Date chooseStartDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        EditText startDate = (EditText) findViewById(R.id.startDate);
        startDate.setText(chooseStartDate.toString());
    }

    /**
     * method for creating a habit from the layout by getting the user's inputs
     * @param v: the layout that it's coming from
     */
    public void CreateHabit(View v) {
        EditText habitName = (EditText) findViewById(R.id.habitName);
        EditText reason = (EditText) findViewById(R.id.reason);

        CreateHabitController.CreateHabit(
                habitName.toString(),
                reason.toString(),
                chooseStartDate,

        );
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
        EditText startDate = (EditText) findViewById(R.id.startDate);
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        chooseStartDate = calendar.getTime();
        startDate.setText(chooseStartDate.toString());
    }
}
