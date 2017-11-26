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
    
}