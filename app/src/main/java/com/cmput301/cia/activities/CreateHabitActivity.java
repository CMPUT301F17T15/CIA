/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 * Created by: Jessica Prieto
 * Date: 04-11-2017
 *
 * Version 1.0
 */

package com.cmput301.cia.activities;

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
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.CreateHabitController;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DatePickerUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/** activity for creating a new habit by letting the user input:
 *      - habit name
 *      - reason
 *      - start date
 *      - frequency (days of the week to do the habit)
 */
public class CreateHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Profile user;
    Date chooseStartDate;
    EditText habitName;
    EditText reason;
    EditText startDate;
    MaterialDayPicker dayPicker;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        chooseStartDate = new Date();
        habitName = (EditText) findViewById(R.id.habitName);
        reason = (EditText) findViewById(R.id.reason);
        startDate = (EditText) findViewById(R.id.startDate);
        dayPicker = (MaterialDayPicker) findViewById(R.id.day_picker);
        startDate.setText(chooseStartDate.toString());

        //ToDo fix spinner activity
        //spinner activity, could be placed in another activity file for better practice
        spinner = (Spinner) findViewById(R.id.habitTypeSpinner);
        final List<String> type = new ArrayList<String>();
        if (getIntent().getStringArrayListExtra("types") == null){
            type.add("Create new type");
        }
        else {
            for (String t : getIntent().getStringArrayListExtra("types")){
                type.add(t);
            }
            type.add("Create new type");
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(), "Selected " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                if (i == type.size() - 1){
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateHabitActivity.this);
                    View mview = getLayoutInflater().inflate(R.layout.dialog_input,null);
                    final EditText minput = (EditText) mview.findViewById(R.id.edit_Type_Input);
                    Button okButton = (Button) mview.findViewById(R.id.Ok_Button);
                    mBuilder.setView(mview);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    okButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            if (!minput.getText().toString().isEmpty()){
                                type.add(0, minput.getText().toString());
                                dialog.dismiss();
                            }else{
                                Toast.makeText(CreateHabitActivity.this, "Please enter the type name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mBuilder.setView(mview);
                    dialog.show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //spinner finished

    }

    /**
     * method for creating a habit from the layout by getting the user's inputs
     * @param v: the layout that it's coming from
     *
     * TODO: take into account the habit type
     */
    public void createHabit(View v) {
        List<MaterialDayPicker.Weekday> daysSelected = dayPicker.getSelectedDays();
        if (daysSelected.size() == 0) {
            Toast.makeText(CreateHabitActivity.this, "Please select at least one day of notification frequency.", Toast.LENGTH_SHORT).show();
        }
        else{
            Habit habit = CreateHabitController.onSaveClicked(
                    habitName.getText().toString(),
                    reason.getText().toString(),
                    chooseStartDate,
                    getPickedDates(daysSelected),
                    spinner.getSelectedItem().toString()
            );

            Intent intent = new Intent();
            intent.putExtra("Habit", habit);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    public void clearInputFields(View v) {
        habitName.setText("");
        reason.setText("");
        startDate.setText("");
        dayPicker.clearSelection();
    }

    /**
     * converts the List<MaterialDayPicker.Weekday> the expected format for dates: LIst<Integer>
     * MaterialDayPicker.Weekday has Monday = 1, ... so this method also fixes the offset
     *
     * @param pickedDates
     * @return
     */
    public List<Integer> getPickedDates(List<MaterialDayPicker.Weekday> pickedDates) {
        List<Integer> outputDatesList = new ArrayList<Integer>();
        for (MaterialDayPicker.Weekday weekday : pickedDates) {
            outputDatesList.add(weekday.ordinal() + 1);
        }

        return outputDatesList;
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

        chooseStartDate = calendar.getTime();
        startDate.setText(chooseStartDate.toString());
    }
}
