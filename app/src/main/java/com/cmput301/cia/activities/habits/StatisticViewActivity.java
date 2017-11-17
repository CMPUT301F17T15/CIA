/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shipin Guan
 * @version 2
 *
 * Created by gsp on 2017/11/13.
 */

public class StatisticViewActivity extends AppCompatActivity {
    private Profile user;
    private TextView typeName;
    private TextView typeNumber;
    private TextView completeNumber;
    private TextView totalNumber;
    private TextView mostMissed;
    private ListView BreakDownList;
    private PieChart pieChart;
    private int[] yData;
    private String[] xData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_view);

        user = (Profile) getIntent().getSerializableExtra("Profile");

        String type = getIntent().getStringExtra("type");
        typeName = (TextView) findViewById(R.id.Type_Name);
        typeName.setText(type);
        typeNumber = (TextView) findViewById(R.id.habitsNumber);
        typeNumber.setText(String.valueOf(user.getHabitsInCategory(type).size()));
        int completeCounter = 0;
        int missCounter = 0;
        int largestMiss = 0;
        List<String> breakdownlist = new ArrayList<String>();

        mostMissed = (TextView) findViewById(R.id.missedMost);
        for (Habit h : user.getHabitsInCategory(type)){
            System.out.println(h.getTitle());
            completeCounter += h.getTimesCompleted();
            missCounter += h.getTimesMissed();
            breakdownlist.add("Habit: " + h.getTitle() + "\nCompleted: " + h.getTimesCompleted() + ". \nMissed: " + h.getTimesMissed() + ".");
            if(h.getTimesMissed() > largestMiss){
                mostMissed.setText(h.getTitle());
                largestMiss = h.getTimesMissed();
            }
        }
        System.out.println(breakdownlist);

        completeNumber = (TextView) findViewById(R.id.completeNumber);
        completeNumber.setText(String.valueOf(completeCounter));
        totalNumber = (TextView) findViewById(R.id.TotalNumber);
        totalNumber.setText(String.valueOf(missCounter));
        if(missCounter == 0) {
            Toast.makeText(StatisticViewActivity.this, "WOW you haven't missed any habits yet!\nKeep up the good work!", Toast.LENGTH_LONG).show();
            mostMissed.setText("Nothing Missed");
        }
        //Break down list view for each habit
        BreakDownList = (ListView) findViewById(R.id.BreakDownListView);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakdownlist);
        BreakDownList.setAdapter(adapter);
        //data for pie chart
        yData = new int[]{missCounter, user.getHabitHistory().size()};
        xData = new String[]{"Total","Complete"};

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(5f);

        addData(pieChart);

    }

    private void addData(PieChart chart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
            xEntrys.add(xData[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Number habits");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.WHITE);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        pieDataSet.setColors(colors);
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}
