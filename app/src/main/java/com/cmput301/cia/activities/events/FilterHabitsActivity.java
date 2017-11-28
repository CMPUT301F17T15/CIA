/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.events;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.cmput301.cia.utilities.FontUtilities;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 27 2017
 *
 * This activity allows the user to pick a habit to filter their habit history with
 */

public class FilterHabitsActivity extends AppCompatActivity {

    // Intent identifier for the incoming habits
    public static final String ID_HABITS = "Habits";

    // Intent identifier for the outgoing selected habit id
    public static final String RETURNED_HABIT_ID = "Habit";

    private ArrayAdapter<Habit> listAdapter;
    private Habit selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);

        Intent data = getIntent();
        List<Habit> habits = (List<Habit>) data.getSerializableExtra(ID_HABITS);

        ListView habitsList = (ListView)findViewById(R.id.filterListView);
        listAdapter = new ArrayAdapter<>(this, R.layout.checkable_list_view, R.id.CheckedTextView, habits);
        habitsList.setAdapter(listAdapter);
        habitsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = listAdapter.getItem(position);
            }
        });

        FontUtilities.applyFontToViews(this, (ViewGroup)findViewById(R.id.filterHabitsLayout));
    }

    @Override
    protected void onStart() {
        super.onStart();
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
