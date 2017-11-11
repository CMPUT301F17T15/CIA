/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license. */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
    /** * Version 2 * Author: Guanfang Dong
 * Date: Nov 5 2017
 *
 * This is history activity.

*  User allows to view their history habits and filter by date and comment.
 */
public class HistoryActivity extends AppCompatActivity {
    // global variables
    private List<HabitEvent> habitList;
    private List<HabitEvent> habitsShowOnScreen;
    private List<String>  habitsShowOnScreen_toString;
    private ArrayAdapter<String> habitsShowOnScreen_adapter;

    private ListView historyList;
    private String ID;
    private EditText filterEditText;
    private Profile user;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ID = getIntent().getExtras().getString("ID");
        user = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, ID);
        habitList = user.getHabitHistory();
        historyList = (ListView) findViewById(R.id.historyList);
        TextView filterStaticText;

        filterEditText = (EditText) findViewById(R.id.filterEditText);

        Button historyEventFilterButton = (Button) findViewById(R.id.historyEventFilterButton);
        Button historyReturnButton  = (Button) findViewById(R.id.historyReturnButton);
        Button filterByType = (Button) findViewById(R.id.filterByType);
        Button filterByComment = (Button) findViewById(R.id.filterByComment);

        habitsShowOnScreen_adapter=new ArrayAdapter<String>(this,R.layout.list_item, habitsShowOnScreen_toString);
        historyList.setAdapter(habitsShowOnScreen_adapter);
        habitsShowOnScreen = habitList;
        habitsShowOnScreen_toString=getListOfString(habitsShowOnScreen,user);
        habitsShowOnScreen_adapter.notifyDataSetChanged();

        filterByType.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String type = new String();
                type = filterEditText.getText().toString();
                habitsShowOnScreen = filterByTypeFunction(habitList, type);
                habitsShowOnScreen_adapter.notifyDataSetChanged();
            }
        });


        filterByComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String comment = new String();
                comment = filterEditText.getText().toString();
                habitsShowOnScreen = filterByCommentFunction(habitList, comment);
                habitsShowOnScreen_adapter.notifyDataSetChanged();
            }
        });

        historyReturnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
    }





    // giving habits and filter by type
    public List<HabitEvent> filterByTypeFunction(List<HabitEvent> habitList, String type){
        List<HabitEvent> sortedList = new ArrayList<HabitEvent>();
        int size = habitList.size();
        int counter = 0;
        while (counter<size){
            if (habitList.get(counter).getTypeId() == type){
                sortedList.add(habitList.get(counter));
            }
            counter++;
        }
        return sortedList;
    }


    // giving habits and filter by comment
    public List<HabitEvent> filterByCommentFunction(List<HabitEvent> habitList, String comment){
        List<HabitEvent> filteredList = new ArrayList<HabitEvent>();
        int size = habitList.size();
        int counter = 0;
        while (counter<size){
            if (habitList.get(counter).getComment()==comment){
                filteredList.add(habitList.get(counter));
            }
            counter++;
        }
        return filteredList;
    }


    public List<String> getListOfString(List<HabitEvent> habitsShowOnScreen, Profile user){
        int size=habitsShowOnScreen.size();
        int counter = 0;
        List <String> returnStr = new ArrayList<String>();
        while (counter<size){
            returnStr.add(getString(habitsShowOnScreen.get(counter),user));
            counter++;
        }
        return returnStr;
    }


    public String getString(HabitEvent event, Profile user){
        String comment = event.getComment();
        String name = new String();
        List<Habit> habits = user.getHabits();
        int size = habits.size();
        int counter = 0;
        while (counter<size){
            List<HabitEvent> habitEvents = habits.get(counter).getEvents();
            int size2 = habitEvents.size();
            int counter2 = 0;
            while (counter2<size2){
                if (habitEvents.get(counter)==event){
                    name = habits.get(counter).getTitle();
                }
                counter2++;
            }
            counter++;
        }
        return "title: "+name+"\ncomment: "+comment;
    }

}
