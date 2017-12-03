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
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Shipin Guan
 * @version 2
 * Created on 2017/11/29.
 *
 * Display details of selected habit.
 */

public class SingleStatisticViewActivity extends AppCompatActivity {

    // Intent identifier for the viewed habit
    public static final String ID_HABIT = "Habit";

    private LineChart lineChart;
    private TextView lastComplete;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_habit_statistic);

        initChart();

        //Information receiving from previous activity
        Habit habit = (Habit)getIntent().getSerializableExtra(ID_HABIT);
        TextView completed = (TextView) findViewById(R.id.CompletedTextView);
        completed.setText("" + habit.getTimesCompleted());

        lastComplete = (TextView) findViewById(R.id.LastCompleteTextView);

        if (habit.getLastCompletionDate() != null)
            lastComplete.setText(DateUtilities.formatDate(habit.getLastCompletionDate()));
        else
            lastComplete.setText("Never");

        TextView miss = (TextView) findViewById(R.id.MissedTextView);
        miss.setText("" + habit.getTimesMissed());

        TextView title = (TextView) findViewById(R.id.HabitNameTextView);
        title.setText(habit.getTitle());

        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");

        List<String> missedHabitDates = new ArrayList<>();
        List<String> completedHabitDates = new ArrayList<>();
        for (HabitEvent event : habit.getEvents())
            completedHabitDates.add(formatter.format(event.getDate()));
        for (Date date : habit.getMissedDates())
            missedHabitDates.add(formatter.format(date));

        Collections.sort(missedHabitDates);
        Collections.sort(completedHabitDates);

        //Data processing for line chart
        List<String> xAxis = new ArrayList<>();

        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(2016, 10, 10);
        habit.setStartDate(calendar.getTime());
        calendar.setTime(habit.getStartDate());
        Date end = new Date();
        while (!DateUtilities.isSameDay(calendar.getTime(), end) && DateUtilities.isBefore(calendar.getTime(), end)){
            dates.add(calendar.getTime());
            xAxis.add(formatter.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }

        int numDataPoints = xAxis.size();
        ArrayList<Entry> yxexs = new ArrayList<>();
        Float[] yAxis = new Float[numDataPoints];

        int completions = 0;
        int misses = 0;

        for (int i = 0; i < dates.size(); ++i){
            if (Collections.binarySearch(missedHabitDates, formatter.format(dates.get(i))) >= 0){
                ++misses;
            } else if (Collections.binarySearch(completedHabitDates, formatter.format(dates.get(i))) >= 0){
                ++completions;
            }

            float percentage = 0.0f;
            if (completions + misses != 0)
                percentage = (completions / (float)(completions + misses)) * 100;

            yAxis[i] = percentage;
        }

        int pos = 0;
        for (float i : yAxis) {
            yxexs.add(new Entry(pos, i));
            pos = pos + 1;
        }

        String[] xaxes = new String[xAxis.size()];
        for (int i = 0; i < xAxis.size(); i++) {
            xaxes[i] = xAxis.get(i).toString();
        }
        XAxis xax = lineChart.getXAxis();
        xax.setValueFormatter(new XValueFormatter(xaxes));
        xax.setGranularity(1);
        xax.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xax.setTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(yxexs, "Progress(1=completed, -1=missed)");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextSize(15f);
        lineDataSet.setValueTextColor(Color.RED);

        dataSets.add(lineDataSet);
        lineChart.setData(new LineData(dataSets));
        lineChart.setVisibleXRangeMaximum(30);
        lineChart.getLineData().setDrawValues(false);
    }

    /**
     * helper for data processing for line chart
     */
    public class XValueFormatter implements IAxisValueFormatter {
        private String[] mvalues;

        public XValueFormatter(String[] values) {
            this.mvalues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mvalues[(int)value];
        }
    }

    private void initChart(){

        //Initial line Chart for visualization of statistic
        lineChart = (LineChart) findViewById(R.id.LineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0.0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
    }
}
