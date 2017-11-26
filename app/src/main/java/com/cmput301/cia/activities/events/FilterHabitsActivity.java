/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 11 2017
 *
 * This activity allows the user to pick a habit to filter their habit history with
 */

public class FilterHabitsActivity extends AppCompatActivity {

    // Intent identifier for the incoming user
    public static final String ID_USER = "User";

    // Intent identifier for the outgoing selected habit id
    public static final String RETURNED_HABIT_ID = "Habit";

    private ListView habitsList;
    private ArrayAdapter<Habit> listAdapter;

    private TextView habitNameText;

    private Profile user;

    private Habit selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);

        Intent data = getIntent();
        user = (Profile) data.getSerializableExtra(ID_USER);

        if (user == null){
            Toast.makeText(this, "Profile could not be retrieved from the database.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        habitNameText = (TextView)findViewById(R.id.filterHabitText);
        habitsList = (ListView)findViewById(R.id.filterListView);

        listAdapter = new ArrayAdapter<>(this, R.layout.list_item, user.getHabits());
        habitsList.setAdapter(listAdapter);

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = listAdapter.getItem(position);
                habitNameText.setText(selected.getTitle());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter = new ArrayAdapter<>(this, R.layout.list_item, user.getHabits());
        habitsList.setAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onFinishClicked(View view){
        Intent intent = new Intent();

        if (selected == null)
            intent.putExtra(RETURNED_HABIT_ID, "");
        else
            intent.putExtra(RETURNED_HABIT_ID, selected.getId());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
