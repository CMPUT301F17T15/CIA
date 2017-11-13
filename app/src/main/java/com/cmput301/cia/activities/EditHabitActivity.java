/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * Created by gsp on 2017/11/12.
 */

public class EditHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Date chooseStartDate;
    private EditText habitName;
    private EditText reason;
    private EditText startDate;
    private MaterialDayPicker dayPicker;
    private Spinner spinner;
    private Profile user;
    private Habit target;
    private String habitId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_detail);
        Intent intent = getIntent();
        habitId = intent.getStringExtra("HabitID");
        String name = intent.getStringExtra("UserName");

        Map<String, String> values = new HashMap<>();
        values.put("name", name);
        user = ElasticSearchUtilities.getObject("profile", Profile.class, values);
        user.getHabitCategories();


        target = user.getHabitById(habitId);

        chooseStartDate = new Date();
        habitName = (EditText) findViewById(R.id.habitName);
        habitName.setText(target.getTitle());
        reason = (EditText) findViewById(R.id.reason);
        reason.setText(target.getReason());
        startDate = (EditText) findViewById(R.id.startDate);
        startDate.setText(target.getStartDate().toString());
        dayPicker = (MaterialDayPicker) findViewById(R.id.day_picker);
        startDate.setText(DateUtilities.formatDate(chooseStartDate));

        //spinner activity, could be placed in another activity file for better practice
        //get the current habit types

        spinner = (Spinner) findViewById(R.id.habitTypeSpinner);
        final List<String> type = new ArrayList<String>();
        if (user.getHabitCategories() == null){
            type.add("Create new type");
        }
        else {
            for (String t : user.getHabitCategories()){
                type.add(t);
            }
            type.remove(type.indexOf(target.getType()));
            type.add(0,target.getType());
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
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditHabitActivity.this);
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
                                Toast.makeText(EditHabitActivity.this, "Please enter the type name", Toast.LENGTH_SHORT).show();
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

    public void saveChange(View view){
        List<MaterialDayPicker.Weekday> daysSelected = dayPicker.getSelectedDays();
        if (daysSelected.size() == 0) {
            Toast.makeText(EditHabitActivity.this, "Please select at least one day of notification frequency.", Toast.LENGTH_SHORT).show();
        } else if (habitName.getText().toString().length() == 0){
            Toast.makeText(EditHabitActivity.this, "The habit title can not be left blank.", Toast.LENGTH_SHORT).show();
        }
        else{
            //ToDo user.save() not saving
            //set changes on current habit.
            target.setTitle(habitName.getText().toString());
            target.setReason(reason.getText().toString());
            target.setStartDate(chooseStartDate);
            target.setDaysOfWeek(getPickedDates(daysSelected));
            target.setType(spinner.getSelectedItem().toString());
            user.save();

            Intent returnIntent = new Intent();
            returnIntent.putExtra("HabitID", habitId);
            setResult(RESULT_OK, returnIntent);
            finish();

        }
    }

    public List<Integer> getPickedDates(List<MaterialDayPicker.Weekday> pickedDates) {
        List<Integer> outputDatesList = new ArrayList<Integer>();
        for (MaterialDayPicker.Weekday weekday : pickedDates) {
            outputDatesList.add(weekday.ordinal() + 1);
        }

        return outputDatesList;
    }

    public void datePickerDialog(View v) {
        DatePickerUtilities datePickerFragment = new DatePickerUtilities();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        chooseStartDate = calendar.getTime();
        startDate.setText(DateUtilities.formatDate(chooseStartDate));
    }
}
