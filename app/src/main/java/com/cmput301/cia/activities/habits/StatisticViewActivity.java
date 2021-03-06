/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.habits;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedAdapterViewClickListener;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shipin Guan
 * @version 2
 *
 * Created on 2017/11/13.
 * Part of the habit statistic
 * Display details of selected habit type/category
 */

public class StatisticViewActivity extends AppCompatActivity {

    private Profile user;

    private ListView BreakDownList;

    private PieChart pieChart;
    private int[] yData;
    private String[] xData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_view);

        user = (Profile) getIntent().getSerializableExtra("Profile");

        final String type = getIntent().getStringExtra("type");
        this.setTitle(type);
        TextView typeNumber = (TextView) findViewById(R.id.habitsNumber);
        typeNumber.setText(String.valueOf(user.getHabitsInCategory(type).size()));
        int completeCounter = 0;
        int missCounter = 0;
        int largestMiss = 0;
        final List<String> breakdownlist = new ArrayList<>();

        TextView mostMissed = (TextView) findViewById(R.id.missedMost);
        List<Habit> habitsInCategory = user.getHabitsInCategory(type);
        for (Habit h : habitsInCategory){
            completeCounter += h.getTimesCompleted();
            missCounter += h.getTimesMissed();
            breakdownlist.add(h.getTitle() + "\nCompleted: " + h.getTimesCompleted() + " \nMissed: " + h.getTimesMissed());
            if(h.getTimesMissed() > largestMiss){
                mostMissed.setText(h.getTitle());
                largestMiss = h.getTimesMissed();
            }
        }

        TextView completeNumber = (TextView) findViewById(R.id.completeNumber);
        completeNumber.setText(String.valueOf(completeCounter));
        TextView totalNumber = (TextView) findViewById(R.id.TotalNumber);
        totalNumber.setText(String.valueOf(missCounter));
        if (missCounter == 0) {
            mostMissed.setText("Nothing Missed");
        }
        //Break down list view for each habit
        BreakDownList = (ListView) findViewById(R.id.BreakDownListView);
        BreakDownList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, breakdownlist);
        BreakDownList.setAdapter(adapter);

        BreakDownList.setOnItemClickListener(new TimedAdapterViewClickListener(){

            @Override
            public void handleClick(View view, int i) {
                Intent intent = new Intent(StatisticViewActivity.this, SingleStatisticViewActivity.class);
                intent.putExtra(SingleStatisticViewActivity.ID_HABIT, user.getHabitsInCategory(type).get(i));
                startActivity(intent);
            }
        });

        //data for pie chart
        yData = new int[]{missCounter, completeCounter};
        xData = new String[]{"Total","Complete"};

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(5f);

        addData();
    }

    private void addData() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
            xEntrys.add(xData[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Category History");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.WHITE);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        pieDataSet.setColors(colors);
        Legend legend = pieChart.getLegend();

        List<LegendEntry> legends = new ArrayList<>();
        LegendEntry red = new LegendEntry();
        red.label = "Missed";
        red.formColor = Color.RED;
        legends.add(red);
        LegendEntry blue = new LegendEntry();
        blue.label = "Completed";
        blue.formColor = Color.BLUE;
        legends.add(blue);
        pieChart.getLegend().setCustom(legends);
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
    }
}
