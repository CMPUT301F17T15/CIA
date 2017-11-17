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
 * Created by gsp on 2017/11/7.
 */

public class StatisticActivity extends AppCompatActivity {

    private Profile user;
    private ListView typeList;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar_statistic);
        setSupportActionBar(toolbar);

        user = (Profile) getIntent().getSerializableExtra("Profile");

        typeList = (ListView)findViewById(R.id.Type_List_View);
        final List<String> types = new ArrayList<String>();
        if (user.getHabitCategories() == null){
            types.add(null);
        }
        else {
            for (String t : user.getHabitCategories()){
                types.add(t);
            }
        }
        adapter = new ArrayAdapter<String>(this, R.layout.list_statistic, types);
        typeList.setAdapter(adapter);

        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StatisticActivity.this, StatisticViewActivity.class);
                intent.putExtra("Profile", user);
                intent.putExtra("type",types.get(i));
                startActivity(intent);
            }
        });
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