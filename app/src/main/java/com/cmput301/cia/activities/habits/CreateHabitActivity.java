/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.fragments.DatePickerFragment;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.DialogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * @author Jessica Prieto
 * @version 3
 *
 * This activity is for creating a new habit by letting the user input:
 *      - habit name
 *      - reason
 *      - start date
 *      - frequency (days of the week to do the habit)
 */
public class CreateHabitActivity extends HabitViewActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toStatisticButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        clearButton.setVisibility(View.VISIBLE);
        setTitle("Create new Habit");
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        finish();
    }
}
