/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;

/**
 * created by Tinghui
 */

public class HabitEventViewActivity extends AppCompatActivity {

    private TextView habitEventName;
    private TextView habitEventType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_view);
        habitEventName = (EditText) findViewById(R.id.EditHabitEventName);
        habitEventType = (EditText) findViewById(R.id.EditHabitEventType);
        Bundle bundle = getIntent().getExtras();
        habitEventType.setText(bundle.getString("HabitEventType"));
        habitEventName.setText(bundle.getString("HabitEventName"));



    }
}
