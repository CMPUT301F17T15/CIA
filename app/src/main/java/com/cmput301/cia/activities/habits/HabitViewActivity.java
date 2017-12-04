/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.fragments.DatePickerFragment;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.views.ClickableEditItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * @author Shipin Guan
 * @version 2
 * Date: Nov 7, 2017
 *
 * This activity allows the user to view the details about one of their habits
 */

public class HabitViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText habitName;
    private EditText habitReason;
    private Spinner habitTypeSpinner;

    private ClickableEditItem dateItem;
    private String StartDate;

    private MaterialDayPicker dayPicker;

    private static final String[] days = {"Sunday\n", "Monday\n", "Tuesday\n", "Wednesday\n", "Thursday\n", "Friday\n", "Saturday\n"};

    private Habit habit;

    protected Button toStatisticButton;
    protected Button deleteButton;
    protected Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);

        toStatisticButton = (Button) findViewById(R.id.toStatistic);
        deleteButton = (Button) findViewById(R.id.DeleteHabitButton);
        saveButton = (Button) findViewById(R.id.SaveHabitButton);

        //Display habit detail
        habitName = (EditText) findViewById(R.id.habitName);
        habitReason = (EditText) findViewById(R.id.habitReason);
        habitTypeSpinner = (Spinner) findViewById(R.id.habitTypeSpinner);

        dateItem = (ClickableEditItem) findViewById(R.id.habitDetailDate);
        StartDate = dateItem.getDynamicText();

        dayPicker = (MaterialDayPicker) findViewById(R.id.day_picker);

        habit = (Habit) getIntent().getSerializableExtra("Habit");

        if (habit == null) {
            habit = new Habit("", "", new Date(), new ArrayList<Integer>(), "");
        }

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

        dateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog(view);
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

        saveButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                saveChange();
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

    /**
     * Creates a dialog so that the user can choose a date instead of typing
     * see: DatePickerFragment
     * @param v: the layout that it's coming from
     */
    public void datePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }



    /** for the date selected
     * @param datePicker : the widget object for selecting a date
     * @param year : the year chosen
     * @param month : the month chosen
     * @param day : the day chosen
     * see: DatePickerFragment
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

        habit.setStartDate(date);

        dateItem.setItemDynamicText(DateUtilities.formatDate(habit.getStartDate()));
    }

    public void saveChange(){
        List<MaterialDayPicker.Weekday> daysSelected = dayPicker.getSelectedDays();
        if (daysSelected.size() == 0) {
            Toast.makeText(this, "Please select at least one day of notification frequency.", Toast.LENGTH_SHORT).show();
        } else if (habitName.getText().toString().length() == 0){
            Toast.makeText(this, "The habit title can not be left blank.", Toast.LENGTH_SHORT).show();
        }
        else{
            //set changes on current habit.
            habit.setTitle(habitName.getText().toString());
            habit.setReason(habitReason.getText().toString());
            habit.setDaysOfWeek(getPickedDates(daysSelected));
            habit.setType(habitTypeSpinner.getSelectedItem().toString());

            Intent returnIntent = new Intent();
            returnIntent.putExtra("Habit", habit);
            setResult(RESULT_OK, returnIntent);
            finish();

        }
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

}
