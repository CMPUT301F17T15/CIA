/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gsp on 2017/11/7.
 */

public class StatisticActivity extends AppCompatActivity {
    //ToDo implement statistic page for each habit type
    private Profile user;
    private TextView Username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);


        String UserID = getIntent().getStringExtra("userName");
        Map<String, String> values = new HashMap<>();
        values.put("name", UserID);
        user = ElasticSearchUtilities.getObject("profile", Profile.class, values);

        System.out.println(user);
        System.out.println(UserID);
        Username = (TextView) findViewById(R.id.Username);
        Username.setText(UserID);

    }
}