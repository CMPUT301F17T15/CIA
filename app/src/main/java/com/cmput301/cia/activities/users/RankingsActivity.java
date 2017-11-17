/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * @version 1
 * Date: Nov 12 2017
 *
 * This activity displays all of the users in the database, ranked by how many points they have
 */

public class RankingsActivity extends AppCompatActivity {

    public static final String ID_ISPOWER = "Type";

    // whether power or overall points are being used (true=power, false=overall)
    private boolean powerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        ListView list = (ListView)findViewById(R.id.rankingsList);
        powerPoints = getIntent().getBooleanExtra(ID_ISPOWER, false);

        List<String> elements = new ArrayList<>();

        List<Profile> profiles = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, new HashMap<String, String>());
        // compare based on points, in descending order
        Collections.sort(profiles, new Comparator<Profile>() {
            @Override
            public int compare(Profile p1, Profile p2) {
                Integer lhsPoints = powerPoints ? p1.getPowerPoints() : p1.getHabitPoints();
                Integer rhsPoints = powerPoints ? p2.getPowerPoints() : p2.getHabitPoints();
                return -1 * lhsPoints.compareTo(rhsPoints);
            }
        });

        int rank = 1;
        for (Profile profile : profiles){
            int points = powerPoints ? profile.getPowerPoints() : profile.getHabitPoints();
            elements.add(rank + ":      " + profile.getName() + ": " + points);
            ++rank;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, elements);
        list.setAdapter(adapter);
    }
}
