/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.controller.TimedAdapterViewClickListener;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shipin Guan
 * @version 2
 *
 * Created on 2017/11/7.
 * Main page for habit statistic
 * Habit sorted by Category
 *
 */

public class StatisticActivity extends AppCompatActivity {

    public static final String ID_USER = "Profile";

    private Profile user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        user = (Profile) getIntent().getSerializableExtra(ID_USER);

        ListView typeList = (ListView)findViewById(R.id.Type_List_View);
        final List<String> types = new ArrayList<>();
        if (user.getHabitCategories() == null){
            types.add(null);
        }
        else {
            for (String t : user.getHabitCategories()){
                types.add(t);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_statistic, types);
        typeList.setAdapter(adapter);

        typeList.setOnItemClickListener(new TimedAdapterViewClickListener(){
            @Override
            public void handleClick(View view, int i) {
                Intent intent = new Intent(StatisticActivity.this, StatisticViewActivity.class);
                intent.putExtra("Profile", user);
                intent.putExtra("type",types.get(i));
                startActivity(intent);
            }
        });
    }
    
}