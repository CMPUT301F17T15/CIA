/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author Adil Malik
 * @version 2
 * Date: Dec 03 2017
 *
 * This activity displays all of the users in the database, ranked by how many points they have
 */

public class RankingsActivity extends AppCompatActivity {

    private ListView overallList;
    private ListView powerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        List<Profile> profiles = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, new HashMap<String, String>());
        initPowerRankings(profiles);
        initOverallRankings(profiles);

        setTitle("Power Rankings");
        findViewById(R.id.rankingsTypeSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overallList.getVisibility() == View.VISIBLE){
                    powerList.setVisibility(View.VISIBLE);
                    overallList.setVisibility(View.INVISIBLE);
                    setTitle("Power Rankings");
                } else {
                    powerList.setVisibility(View.INVISIBLE);
                    overallList.setVisibility(View.VISIBLE);
                    setTitle("Overall Rankings");
                }
            }
        });
    }

    private void initPowerRankings(List<Profile> profiles){

        powerList = (ListView)findViewById(R.id.rankingsPowerList);

        List<String> elements = new ArrayList<>();
        // compare based on points, in descending order
        Collections.sort(profiles, new Comparator<Profile>() {
            @Override
            public int compare(Profile p1, Profile p2) {
                Integer lhsPoints = p1.getPowerPoints();
                Integer rhsPoints = p2.getPowerPoints();
                return -1 * lhsPoints.compareTo(rhsPoints);
            }
        });

        int rank = 1;
        for (Profile profile : profiles){
            int points = profile.getPowerPoints();
            elements.add(rank + ":      " + profile.getName() + ": " + points);
            ++rank;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, elements);
        powerList.setAdapter(adapter);
    }

    private void initOverallRankings(List<Profile> profiles){

        overallList = (ListView)findViewById(R.id.rankingsList);
        List<String> elements = new ArrayList<>();
        // compare based on points, in descending order
        Collections.sort(profiles, new Comparator<Profile>() {
            @Override
            public int compare(Profile p1, Profile p2) {
                Integer lhsPoints = p1.getHabitPoints();
                Integer rhsPoints = p2.getHabitPoints();
                return -1 * lhsPoints.compareTo(rhsPoints);
            }
        });

        int rank = 1;
        for (Profile profile : profiles){
            int points = profile.getHabitPoints();
            elements.add(rank + ":      " + profile.getName() + ": " + points);
            ++rank;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, elements);
        overallList.setAdapter(adapter);

    }
}
