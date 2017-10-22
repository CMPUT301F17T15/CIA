/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;

import org.w3c.dom.ls.LSException;

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
    List<Date>missDates;
    List<Habit>filterByCommentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    // giving habits and sort by date, unfinished.
    /*public List<HabitEvent> sortByDate(List<HabitEvent> habits){
        List<HabitEvent>habit=habits;
        return habit;
    }
    // giving habits and filter by type, unfinished.
    public List<HabitEvent> filterByType(List<HabitEvent> habits, String type){
        List<HabitEvent>habitfilterByTypeList=habits;
        return habitfilterByTypeList;
    }
    // giving habits and get the missdate, unfinished.
    public List<Date> getMissedDates(List<HabitEvent> habits) {
        int size = habits.size();
        int a = 0;
        while (a < size) {
            HabitEvent testHabit = habits.get(a);
            this.missDates.add(testHabit.getMissedDates().get(0));
            a = a + 1;
        }
        return this.missDates;
    }

    // giving habits and filter by comment, unfinished.
    public List<Habit> filterByComment(List<Habit> habits, String comment){
        int size = habits.size();
        int a = 0;
        while (a < size) {
            Habit testHabit = habits.get(a);
            if (testHabit.getD()==comment) {
                this.filterByCommentList.add(testHabit);
            }
            a = a + 1;
        }
        return this.filterByCommentList;
    }*/
}
