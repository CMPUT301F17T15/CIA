/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.HabitEvent;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Version 2
 * Author: Guanfang Dong
 * Date: Oct 21 2017
 *
 * This is history activity.
 *  User allows to view their history habits and filter by date and comment.
 */
public class HistoryActivity extends AppCompatActivity {
    // global variables
    private ArrayList<HabitEvent> habitList;
    private ArrayList<HabitEvent> habitsShowOnScreen;
    private ArrayAdapter<HabitEvent> habitsShowOnScreen_adapter;
    private ArrayList<HabitEvent> filterByTime;
    private ArrayList<HabitEvent> filterByTypeList;
    private ArrayList<HabitEvent> filterByCommentList;

    private ListView historyList;
//    private TextView filterStaticText;
    private EditText filterEditText;
//    private Button historyEventFilterButton;
//    private Button historyReturnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = (ListView) findViewById(R.id.historyList);
        TextView filterStaticText;
        filterEditText = (EditText) findViewById(R.id.filterEditText);


        Button historyEventFilterButton = (Button) findViewById(R.id.historyEventFilterButton);
        Button historyReturnButton  = (Button) findViewById(R.id.historyReturnButton);
        Button filterByType = (Button) findViewById(R.id.filterByType);
        Button filterByComment = (Button) findViewById(R.id.filterByComment);



        filterByType.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String type = new String();
                type = filterEditText.getText().toString();
                filterByTypeList = filterByTypeFunction(habitList, type);
                habitsShowOnScreen = filterByTypeList;
                habitsShowOnScreen_adapter.notifyDataSetChanged();
            }
        });


        filterByComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String comment = new String();
                comment = filterEditText.getText().toString();
                filterByCommentList = filterByCommentFunction(habitList, comment);
                habitsShowOnScreen = filterByCommentList;
                habitsShowOnScreen_adapter.notifyDataSetChanged();
            }
        });


    }


    // giving habits and sort by date, too hard!!!
//    public ArrayList<HabitEvent> sortByDate(ArrayList<HabitEvent> habitList){
//        int size = habitList.size();
//
//    }


    // giving habits and filter by type
    public ArrayList<HabitEvent> filterByTypeFunction(ArrayList<HabitEvent> habitList, String type){
        ArrayList<HabitEvent> sortedList = new ArrayList<HabitEvent>();
        int size = habitList.size();
        int counter = 0;
        while (counter<size){
            if (habitList.get(counter).getTypeId() == type){
                sortedList.add(habitList.get(counter));
            }
        }
        return sortedList;
    }


    // giving habits and filter by comment
    public ArrayList<HabitEvent> filterByCommentFunction(ArrayList<HabitEvent> habitList, String comment){
        ArrayList<HabitEvent> filteredList = new ArrayList<HabitEvent>();
        int size = habitList.size();
        int counter = 0;
        while (counter<size){
            if (habitList.get(counter).getComment()==comment){  //May Wrong, wait for class function
                filteredList.add(habitList.get(counter));
            }
        }
        return sortedList;
    }


















    // giving habits and get the missdate, unfinished.
//    public List<Date> getMissedDates(List<HabitEvent> habits) {
//        int size = habits.size();
//        int a = 0;
//        while (a < size) {
//            HabitEvent testHabit = habits.get(a);
//            this.missDates.add(testHabit.getMissedDates().get(0));
//            a = a + 1;
//        }
//        return this.missDates;
//    }

    // giving habits and filter by comment, unfinished.

}
