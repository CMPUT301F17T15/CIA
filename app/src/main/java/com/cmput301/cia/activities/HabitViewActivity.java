/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;

import org.w3c.dom.Text;

/**
 * Created by gsp on 2017/11/7.
 */

public class HabitViewActivity extends AppCompatActivity{
    //ToDo display detail of selected habit
    private TextView habitName;
    private TextView habitType;
    private TextView habitReason;
    private TextView habitStartDate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);

        //Create custom tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar_habit_detail);
        setSupportActionBar(toolbar);


        habitName = (TextView) findViewById(R.id.EditHabitName);
        habitType = (TextView) findViewById(R.id.EditHabitType);
        habitReason = (TextView) findViewById(R.id.EditHabitReason);
        habitStartDate = (TextView) findViewById(R.id.StartingDate);

        Bundle bundle = getIntent().getExtras();
        habitType.setText(bundle.getString("HabitType"));
        habitName.setText(bundle.getString("HabitName"));
        habitReason.setText(bundle.getString("HabitReason"));
        habitStartDate.setText(bundle.getString("StartingDate"));

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
