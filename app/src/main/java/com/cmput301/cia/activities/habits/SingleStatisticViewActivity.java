/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gsp on 2017/11/29.
 */

public class SingleStatisticViewActivity extends AppCompatActivity{
    private Profile user;
    private TextView title;
    private String habitID;
    private TextView userName;
    private Habit habit;
    private LineChart lineChart;
    private Date startDate;
    private List<Date> getMissedDates;
    private List<Integer> weekdays;
    private TextView completed;
    private TextView miss;
    private TextView lastComplete;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_habit_statistic);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        lineChart = (LineChart) findViewById(R.id.LineChart);
        user = (Profile) getIntent().getSerializableExtra("Profile");
        habitID = getIntent().getStringExtra("HabitID");
        habit = user.getHabitById(habitID);

        completed = (TextView) findViewById(R.id.CompletedTextView);
        completed.setText("" + habit.getTimesCompleted());

        lastComplete = (TextView) findViewById(R.id.LastCompleteTextView);

        lastComplete.setText(formatter.format(habit.getLastCompletionDate()).toString());

        miss = (TextView) findViewById(R.id.MissedTextView);
        miss.setText("" + habit.getTimesMissed());

        userName = (TextView) findViewById(R.id.UserNameTextView);
        userName.setText(user.getName());

        title = (TextView) findViewById(R.id.HabitNameTextView);
        title.setText(habit.getTitle());


        List<String> xAxis = new ArrayList<>();

        habit.setStartDate(new Date(117, 10, 25));
        weekdays = habit.getDaysOfWeek();
        startDate = habit.getStartDate();

        ArrayList<String> missed = new ArrayList<>();
        getMissedDates = habit.getMissedDates();
        for (Date date : getMissedDates){
            missed.add((new DateTime(formatter.format(date)).toString("MMM-dd")));
        }

        DateTime start = new DateTime(formatter.format(startDate));
        DateTime end = new DateTime(formatter.format(new Date()));
        for (DateTime d = start; d.isBefore(end)|| d.isEqual(end); d = d.plusDays(1)){
            xAxis.add(d.toString("MMM-dd"));
        }
        int numDataPoints = xAxis.size();
        ArrayList<Entry> yxexs = new ArrayList<>();
        Integer[] yAxis = new Integer[numDataPoints];
        Arrays.fill(yAxis, new Integer(1));

        for (String dt : missed){
            if (xAxis.contains(dt)){
                yAxis[xAxis.indexOf(dt)] = -1;
            }
        }
        int pos = 0;
        for (int i: yAxis){
            yxexs.add(new Entry(i,pos));
            pos = pos + 1;
        }

        String[] xaxes = new String[xAxis.size()];
        for (int i = 0; i<xAxis.size();i++){
            xaxes[i] = xAxis.get(i).toString();
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(yxexs, "Missed");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.BLUE);
        dataSets.add(lineDataSet);
        lineChart.setData(new LineData(dataSets));
        lineChart.setVisibleXRangeMaximum(30);



    }

}
