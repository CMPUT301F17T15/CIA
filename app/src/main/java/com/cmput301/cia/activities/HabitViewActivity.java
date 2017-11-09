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

/**
 * Created by gsp on 2017/11/7.
 */

public class HabitViewActivity extends AppCompatActivity{
    //ToDo display detail of selected habit
    private TextView habitName;
    private TextView habitType;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);

        //Create custom tool bar
        Toolbar HB_toolbar = (Toolbar) findViewById(R.id.app_toolbar_habit_detail);
        setSupportActionBar(HB_toolbar);


        habitName = (EditText) findViewById(R.id.EditHabitName);
        habitType = (EditText) findViewById(R.id.EditHabitType);

        Bundle bundle = getIntent().getExtras();
        habitType.setText(bundle.getString("HabitType"));
        habitName.setText(bundle.getString("HabitName"));

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
