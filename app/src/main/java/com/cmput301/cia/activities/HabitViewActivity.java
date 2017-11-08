/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit_detail);
        habitName = (EditText) findViewById(R.id.EditHabitName);
        habitType = (EditText) findViewById(R.id.EditHabitType);

        Bundle bundle = getIntent().getExtras();
        habitType.setText(bundle.getString("HabitType"));
        habitName.setText(bundle.getString("HabitName"));

    }
}
