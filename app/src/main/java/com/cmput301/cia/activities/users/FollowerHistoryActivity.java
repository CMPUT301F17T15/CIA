/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.events.HabitEventViewActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guanfang
 * @version 1
 * Date: Nov 25, 2017
 *
 * This activity displays the recent habit history of all the people the user is following
 */

public class FollowerHistoryActivity extends AppCompatActivity {

    private Profile user;
    private List<Pair<HabitEvent, String>> followed_history;
    private ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_history);

        final Intent intent = getIntent();
        user = (Profile) intent.getSerializableExtra(SearchUsersActivity.ID_USER);
        followed_history = user.getFollowedHabitHistory();
        historyList = (ListView) findViewById(R.id.followed_history);
        Button goBack = (Button) findViewById(R.id.back);
        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FollowerHistoryActivity.this, HomePageActivity.class);
                intent.putExtra(SearchUsersActivity.ID_USER, user);
                startActivity(intent);
            }
        });

//        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String user_name = followed_history.get(position).second;
//                Intent intent = new Intent(FollowerHistoryActivity.this, SendMessageActivity.class);
//                intent.putExtra("name",user_name);
//                intent.putExtra("pos",position);
//                intent.putExtra(SearchUsersActivity.ID_USER, user);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        display();
    }

    /**
     * display every habitevent on screen
     */
    private void display(){
        List<String> habitList = new ArrayList<>(this.followed_history.size());
        for (Pair<HabitEvent, String> event : this.followed_history) {
            ElasticSearchUtilities elasticSearchUtilities = new ElasticSearchUtilities();
            Habit habit = elasticSearchUtilities.getObject("habit", Habit.class, event.first.getHabitId()).first;
            habitList.add(event.second+ " Completed " + habit.getTitle() + " on " + event.first.getDate());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, habitList);
        historyList.setAdapter(adapter);
    }
}
